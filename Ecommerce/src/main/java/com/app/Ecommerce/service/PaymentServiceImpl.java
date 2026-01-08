package com.app.Ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.Ecommerce.enums.OrderStatus;
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
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final StockNotificationService stockNotificationService;
	private final ProductRepository productRepository;
	private final OrderItemRepository orderItemRepository;
	public PaymentServiceImpl(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository, StockNotificationService stockNotificationService,
			 ProductRepository productRepository, OrderItemRepository orderItemRepository) {

		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
		this.stockNotificationService = stockNotificationService;
		this.productRepository = productRepository;
		this.orderItemRepository = orderItemRepository;
	}
	@Override
	@Transactional
	public Map<String, Object> createCheckoutSession(String email) throws StripeException {
		
	    User buyer = userRepository.findByEmailId(email)
	            .orElseThrow(() -> new RuntimeException("Buyer not found with email: " + email));

	    Cart cart = cartRepository.findByUser(buyer)
	            .orElseThrow(() -> new RuntimeException("Cart not found for user: " + buyer.getName()));

	    if (cart.getItems().isEmpty()) throw new RuntimeException("Cart empty");

	    System.out.println("CART ITEMS COUNT: " + cart.getItems().size());
	    List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
	    for (CartItem item : cart.getItems()) {
	        Product product = item.getProduct();
	        long priceInCents = (long) (product.getPrice() * 100);

	        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
	                .setPriceData(
	                        SessionCreateParams.LineItem.PriceData.builder()
	                                .setCurrency("usd")
	                                .setUnitAmount(priceInCents)
	                                .setProductData(
	                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                                                .setName(product.getName())
	                                                .setDescription(product.getDescription())
	                                                .build()
	                                ).build()
	                )
	                .setQuantity((long) item.getQuantity())
	                .build();

	        lineItems.add(lineItem);
	    }


	    SessionCreateParams params = SessionCreateParams.builder()
	            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
	            .setMode(SessionCreateParams.Mode.PAYMENT)
	            .setSuccessUrl("http://localhost:4200/payment/success")
	            .setCancelUrl("http://localhost:4200/payment/cancel")
	            .addAllLineItem(lineItems)
	            .build();

	    Session session = Session.create(params);


	    Orders order = new Orders();
	    order.setBuyer(buyer);
	    order.setStatus(OrderStatus.PENDING);
	    order.setSessionId(session.getId());
	    order.setTimestamp(LocalDateTime.now());
	    order.setTotal(cart.getItems()
	            .stream()
	            .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
	            .sum());
	    Orders saved = orderRepository.save(order);


	    stockNotificationService.trackLiveOrder(saved.getOrderId(), saved.getStatus().name());

	    
	    for (CartItem item : cart.getItems()) {
	        Product p = item.getProduct();
	        if (p.getStock() < item.getQuantity())
	            throw new RuntimeException("Out of stock for: " + p.getName());

//	        p.setStock(p.getStock() - item.getQuantity());
//	        productRepository.save(p);

	        stockNotificationService.notifyStockChange(p.getProdId(), p.getStock());

	        OrderItem oi = new OrderItem();
	        oi.setOrder(saved);
	        oi.setProduct(p);
	        oi.setQuantity(item.getQuantity());
	        orderItemRepository.save(oi);
	    }


//	    cart.getItems().clear();
//	    cart.setTotal(0);
//	    cartRepository.save(cart);

	   
	    Map<String, Object> result = new HashMap<>();
	    result.put("sessionId", session.getId());
	    result.put("url", session.getUrl());
	    return result;
	}

	
}
