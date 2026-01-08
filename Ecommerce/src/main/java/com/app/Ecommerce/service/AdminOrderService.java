package com.app.Ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.repository.OrderRepository;

@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;

    public AdminOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll(
            Sort.by(Sort.Direction.DESC, "timestamp")
        );
    }

    public Orders getOrderById(int id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
