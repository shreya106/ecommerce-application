package com.app.Ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.Ecommerce.enums.OrderStatus;
import com.app.Ecommerce.exception.ResourceNotFoundException;
import com.app.Ecommerce.model.Cart;
import com.app.Ecommerce.model.CartItem;
import com.app.Ecommerce.model.OrderItem;
import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.CartRepository;
import com.app.Ecommerce.repository.OrderItemRepository;
import com.app.Ecommerce.repository.OrderRepository;
import com.app.Ecommerce.repository.ProductRepository;
import com.app.Ecommerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StockNotificationService stockNotificationService;

    @InjectMocks
    private OrderServiceImpl orderService;

    // SUCCESSFUL CHECKOUT
    @Test
    void checkout_successful() {
        String email = "buyer@test.com";

        User buyer = new User();
        buyer.setEmailId(email);

        Product product = new Product();
        product.setProdId(1);
        product.setStock(10);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        List<CartItem> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);
        cart.setTotal(2000);

        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(buyer));
        when(cartRepository.findByUser(buyer)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Orders.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Orders result = orderService.checkout(email);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(8, product.getStock());

        verify(orderRepository).save(any(Orders.class));
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(productRepository).save(product);

        // FIXED VERIFICATION
        verify(stockNotificationService).trackLiveOrder(
                anyInt(), eq(OrderStatus.PENDING.name())
        );
        verify(stockNotificationService).notifyStockChange(
                eq(product.getProdId()), eq(8)
        );

        verify(cartRepository).save(cart);
    }

    // BUYER NOT FOUND
    @Test
    void checkout_fails_when_buyer_not_found() {
        when(userRepository.findByEmailId(any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.checkout("missing@test.com"));
    }

    // CART NOT FOUND
    @Test
    void checkout_fails_when_cart_not_found() {
        User buyer = new User();

        when(userRepository.findByEmailId(any()))
                .thenReturn(Optional.of(buyer));
        when(cartRepository.findByUser(buyer))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.checkout("buyer@test.com"));
    }

    // EMPTY CART
    @Test
    void checkout_fails_when_cart_empty() {
        User buyer = new User();

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(userRepository.findByEmailId(any()))
                .thenReturn(Optional.of(buyer));
        when(cartRepository.findByUser(buyer))
                .thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class,
                () -> orderService.checkout("buyer@test.com"));
    }

    // OUT OF STOCK
    @Test
    void checkout_fails_when_out_of_stock() {
        User buyer = new User();

        Product product = new Product();
        product.setStock(1);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5);

        List<CartItem> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        when(userRepository.findByEmailId(any()))
                .thenReturn(Optional.of(buyer));
        when(cartRepository.findByUser(buyer))
                .thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class,
                () -> orderService.checkout("buyer@test.com"));

        verify(productRepository, never()).save(any());
        verify(orderItemRepository, never()).save(any());
    }
}
