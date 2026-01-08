package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class ProductResponse {
	private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String category;
    private String imageUrl;
	
}
