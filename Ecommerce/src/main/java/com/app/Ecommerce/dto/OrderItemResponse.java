package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
	 private String productName;
	    private int quantity;
	    private String imageUrl;
	    
	    
	    public OrderItemResponse(String productName, int quantity,String imageUrl) {
	        this.productName = productName;
	        this.quantity = quantity;
	        this.imageUrl = imageUrl;
	    }

}
