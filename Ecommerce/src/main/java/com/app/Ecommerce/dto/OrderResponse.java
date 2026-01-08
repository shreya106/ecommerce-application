package com.app.Ecommerce.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {
	private int orderId;
    private String status;
    private double total;
    private List<OrderItemResponse> items;
    
    public OrderResponse(int orderId, String status, double total, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.status = status;
        this.total = total;
        this.items = items;
    }
}
