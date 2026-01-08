package com.app.Ecommerce.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartResponse {
	 private double total;
	 private List<CartItemResponse> items;
	private int cartId;
}
