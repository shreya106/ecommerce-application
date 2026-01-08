package com.app.Ecommerce.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.service.OrderService;
import com.app.Ecommerce.service.SellerOrderService;

@RestController
@RequestMapping("/api/seller/orders")
@PreAuthorize("hasRole('SELLER')")
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;
    private final OrderService orderService;

    public SellerOrderController(
            SellerOrderService sellerOrderService,
            OrderService orderService
    ) {
        this.sellerOrderService = sellerOrderService;
        this.orderService = orderService;
    }


    private String email() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
    }

    @GetMapping
    public List<Orders> getOrders() {
        return sellerOrderService.getSellerOrders(email());
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(
            @PathVariable int id,
            @RequestParam String status
    ) {
        orderService.updateOrderStatus(id, status);
    }
}
