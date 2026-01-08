package com.app.Ecommerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.dto.AdminAnalyticsResponse;
import com.app.Ecommerce.dto.AdminOrderResponse;
import com.app.Ecommerce.dto.AdminProductResponse;
import com.app.Ecommerce.dto.AdminUserResponse;
import com.app.Ecommerce.service.AdminAnalyticsService;
import com.app.Ecommerce.service.AdminService;
import com.app.Ecommerce.service.OrderService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;
    private final AdminAnalyticsService service;

    public AdminController(AdminService adminService, OrderService orderService,AdminAnalyticsService service) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.service = service;
    }

  
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
    	
        return ResponseEntity.ok(adminService.getAllUsers());
    }
    
    

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/products")
    public ResponseEntity<List<AdminProductResponse>> getAllProducts() {
        return ResponseEntity.ok(adminService.getAllProducts());
    }

    @PutMapping("/products/{id}/toggle")
    public ResponseEntity<String> toggleProductActive(@PathVariable int id) {
        adminService.toggleProductActive(id);
        return ResponseEntity.ok("Product status toggled");
    }

   
    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
        return ResponseEntity.ok(adminService.getAllOrders());
    }


    @PutMapping("/orders/{id}/status/{status}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int id, @PathVariable String status) {
        adminService.updateOrderStatus(id, status);
        orderService.updateOrderStatus(id, status); 
        return ResponseEntity.ok("Order status updated");
    }

    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
    
    @PutMapping("/users/{id}/toggle")
    public ResponseEntity<String> toggleUser(@PathVariable int id) {
        adminService.toggleUserActive(id);
        return ResponseEntity.ok("User status toggled");
    }
    
    
}
