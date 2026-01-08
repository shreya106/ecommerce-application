package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class AdminOrderResponse {

	private int orderId;
    private String buyerEmail;
    private double total;
    private String status;
}
