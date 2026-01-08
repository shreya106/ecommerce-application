package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class SellerRequest {

	private String name;
	private String description;
	private double price;
	private int stock;
	private String categories;
	private Boolean isActive;
}
