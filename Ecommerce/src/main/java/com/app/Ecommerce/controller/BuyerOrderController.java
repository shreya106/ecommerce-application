package com.app.Ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.dto.OrderResponse;
import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.service.OrderService;

@RestController
@RequestMapping("/api/buyer/orders")
public class BuyerOrderController {

	private final OrderService orderService;
	
	public BuyerOrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	private String email() { return SecurityContextHolder.getContext().getAuthentication().getName(); }
	
	@PostMapping("/checkout")
	public ResponseEntity<Orders> checkout() {
		return ResponseEntity.ok(orderService.checkout(email()));
	}
	
	@GetMapping
	public ResponseEntity<List<Orders>> getOrders() {
		return ResponseEntity.ok(orderService.getOrders(email()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable int id) {
		return ResponseEntity.ok(orderService.getOrderById(id, email()));
		
	}
	
	
	
}
