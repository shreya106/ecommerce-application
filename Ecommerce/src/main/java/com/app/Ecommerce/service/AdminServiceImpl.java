package com.app.Ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.Ecommerce.dto.AdminOrderResponse;
import com.app.Ecommerce.dto.AdminProductResponse;
import com.app.Ecommerce.dto.AdminUserResponse;
import com.app.Ecommerce.enums.OrderStatus;
import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.OrderRepository;
import com.app.Ecommerce.repository.ProductRepository;
import com.app.Ecommerce.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {


    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public AdminServiceImpl(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(u -> {
                AdminUserResponse dto = new AdminUserResponse();
                dto.setUserId(u.getId());
                dto.setName(u.getName());
                dto.setEmail(u.getEmailId());
                dto.setRole(u.getRole().name());
                dto.setVerified(u.isVerified());
                return dto;
            })
            .toList();
    }


    @Override
    public void toggleUserActive(int id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
    	User user = userRepository.findById(id)
    	        .orElseThrow(() -> new RuntimeException("User not found"));

    	user.setIsActive(!user.getIsActive());
    	
    	    userRepository.save(user);
    }
    @Override
    public List<AdminProductResponse> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(p -> {
                AdminProductResponse dto = new AdminProductResponse();
                dto.setId(p.getProdId());
                dto.setName(p.getName());
                dto.setCategories(p.getCategories());
                dto.setPrice(p.getPrice());
                dto.setStock(p.getStock());
                dto.setActive(p.getIsActive());
                dto.setSellerEmail(p.getSeller().getEmailId());
                return dto;
            })
            .toList();
    }


    @Override
    public void toggleProductActive(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(!product.getIsActive());
        productRepository.save(product);
    }


    @Override
    public List<AdminOrderResponse> getAllOrders() {
        return orderRepository.findAll()
            .stream()
            .map(o -> {
                AdminOrderResponse dto = new AdminOrderResponse();
                dto.setOrderId(o.getOrderId());
                dto.setBuyerEmail(o.getBuyer().getEmailId());
                dto.setTotal(o.getTotal());
                dto.setStatus(o.getStatus().name());
                return dto;
            })
            .toList();
    }


    @Override
    public void updateOrderStatus(int id, String status) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderRepository.save(order);
    }

    
    @Override
    public Map<String, Object> getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.findAll()
                .stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETE)
                .mapToDouble(Orders::getTotal)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalProducts", totalProducts);
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        return stats;
    }
}