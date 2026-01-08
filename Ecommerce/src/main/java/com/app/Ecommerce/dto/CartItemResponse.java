package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class CartItemResponse {

	private int cartItemId;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String imageUrl;
}
