package com.app.Ecommerce.service;

import com.app.Ecommerce.dto.CartResponse;
import com.app.Ecommerce.model.Cart;

public interface CartService {

	public CartResponse addToCart(String email, int productId, int quantity);
	public CartResponse viewCart(String email);
	public void updateQuantity(int itemId, int quantity);
	public void removeItem(int itemId, String email);
}
