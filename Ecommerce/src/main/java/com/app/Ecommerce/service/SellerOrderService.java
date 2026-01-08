package com.app.Ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.Ecommerce.enums.OrderStatus;
import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.repository.OrderRepository;

@Service
public class SellerOrderService {

    private final OrderRepository orderRepository;

    public SellerOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Orders> getSellerOrders(String email) {
        return orderRepository.findSellerOrders(email);
    }

    public Orders updateStatus(int orderId, OrderStatus status) {
        Orders order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}