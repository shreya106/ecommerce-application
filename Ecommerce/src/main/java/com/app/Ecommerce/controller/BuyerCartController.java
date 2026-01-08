package com.app.Ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.dto.CartResponse;
import com.app.Ecommerce.model.Cart;
import com.app.Ecommerce.service.CartService;

@RestController
@RequestMapping("/api/buyer/cart")
public class BuyerCartController {
	
	
	private final CartService cartService;

    public BuyerCartController(CartService cartService) { this.cartService = cartService; }

    
    private String getEmail() {
    	return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestParam int productId, @RequestParam int quantity){
    	return ResponseEntity.ok(cartService.addToCart(getEmail(), productId, quantity));
    	
    }
    
    @GetMapping
    public ResponseEntity<CartResponse> viewCart() {
        return ResponseEntity.ok(cartService.viewCart(getEmail()));
    }
    
    @PatchMapping("/{itemId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable int itemId, @RequestParam int quantity) {
        cartService.updateQuantity(itemId, quantity); return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable int itemId) {
        cartService.removeItem(itemId, getEmail()); return ResponseEntity.noContent().build();
    }
}
