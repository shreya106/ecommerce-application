package com.app.Ecommerce.service;

import java.util.List;

import com.app.Ecommerce.dto.OrderResponse;
import com.app.Ecommerce.model.Orders;

public interface OrderService {
 
	public Orders checkout(String email);
	public List<Orders> getOrders(String email);
	 
	public OrderResponse getOrderById(int orderId, String email);
	//public void updateOrderStatus(int id, String status);
	public void updateOrderStatus(int orderId, String status);
}
