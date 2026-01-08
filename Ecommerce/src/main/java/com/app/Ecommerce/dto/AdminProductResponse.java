package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class AdminProductResponse {
	private int id;
    private String name;
    private String categories;
    private double price;
    private int stock;
    private boolean active;
    private String sellerEmail;

}
