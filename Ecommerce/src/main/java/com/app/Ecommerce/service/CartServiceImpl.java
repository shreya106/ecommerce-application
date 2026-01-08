package com.app.Ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.Ecommerce.dto.CartItemResponse;
import com.app.Ecommerce.dto.CartResponse;
import com.app.Ecommerce.exception.ResourceNotFoundException;
import com.app.Ecommerce.model.Cart;
import com.app.Ecommerce.model.CartItem;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.CartItemRepository;
import com.app.Ecommerce.repository.CartRepository;
import com.app.Ecommerce.repository.ProductRepository;
import com.app.Ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService{
	
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, 
			UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public CartResponse addToCart(String email, int productId, int quantity) {
		User buyer = userRepository.findByEmailId(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		
		if(product.getStock() < quantity) {
			throw new RuntimeException("Insufficient stock");
		}
		Cart cart = cartRepository.findByUser(buyer).orElseGet(() -> {
			Cart c  = new Cart();
			c.setUser(buyer);
			c.setTotal(0);
			return cartRepository.save(c);
			
		});
		
		CartItem item = new CartItem();
		
		item.setCart(cart);
		item.setQuantity(quantity);
		item.setProduct(product);
		cartItemRepository.save(item);
		
		cart.setTotal(cart.getTotal()+product.getPrice() * quantity);
		cartRepository.save(cart);
		return mapToResponse(cart);
		
	}

	@Override
	public CartResponse viewCart(String email) {

	    User user = userRepository.findByEmailId(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Cart cart = cartRepository.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Cart not found"));

	    CartResponse response = new CartResponse();
	    response.setCartId(cart.getCartId());
	    response.setTotal(cart.getTotal());

	    List<CartItemResponse> items = cart.getItems().stream().map(item -> {
	        CartItemResponse dto = new CartItemResponse();
	        dto.setCartItemId(item.getCartItemId());
	        dto.setProductId(item.getProduct().getProdId());
	        dto.setProductName(item.getProduct().getName());
	        dto.setPrice(item.getProduct().getPrice());
	        dto.setQuantity(item.getQuantity());
	        dto.setImageUrl(item.getProduct().getImageUrl());
	        return dto;
	    }).toList();

	    response.setItems(items);
	    return response;
	}


	@Override
	@Transactional
	public void updateQuantity(int itemId, int quantity) {
		CartItem cartItem = cartItemRepository.findById(itemId)
		        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

		    cartItem.setQuantity(quantity);
		    cartItemRepository.save(cartItem);

		    Cart cart = cartItem.getCart();

		    double total = cart.getItems().stream()
		        .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
		        .sum();

		    cart.setTotal(total);
		    cartRepository.save(cart);
		
	}

	
	@Override
	@Transactional
	public void removeItem(int cartItemId, String email) {

	    User buyer = userRepository.findByEmailId(email)
	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    Cart cart = cartRepository.findByUser(buyer)
	        .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

	   
	    if (!(cartItem.getCart().getCartId() == cart.getCartId())) {
	        throw new RuntimeException("Unauthorized cart item");
	    }

	    cart.getItems().remove(cartItem);
	    cartItemRepository.delete(cartItem);

	    double total = cart.getItems().stream()
	        .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
	        .sum();

	    cart.setTotal(total);
	    cartRepository.save(cart);
	}
	
	private CartResponse mapToResponse(Cart cart) {
	    CartResponse response = new CartResponse();
	    response.setTotal(cart.getTotal());

	    List<CartItemResponse> items = cart.getItems().stream().map(item -> {
	        CartItemResponse dto = new CartItemResponse();
	        dto.setCartItemId(item.getCartItemId());
	        dto.setProductId(item.getProduct().getProdId());
	        dto.setProductName(item.getProduct().getName());
	        dto.setPrice(item.getProduct().getPrice());
	        dto.setQuantity(item.getQuantity());
	        return dto;
	    }).toList();

	    response.setItems(items);
	    return response;
	}

	

}
