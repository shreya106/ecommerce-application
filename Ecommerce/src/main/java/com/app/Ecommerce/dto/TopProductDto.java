package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class TopProductDto {
    private String productName;
    private long quantitySold;
    
    public TopProductDto(String productName,long quantitySold) {
    	this.productName = productName;
    	this.quantitySold = quantitySold;
    }
}
