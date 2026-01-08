package com.app.Ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.Ecommerce.dto.AdminAnalyticsResponse;
import com.app.Ecommerce.dto.TopProductDto;
import com.app.Ecommerce.repository.OrderItemRepository;
import com.app.Ecommerce.repository.OrderRepository;

@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public AdminAnalyticsServiceImpl(
        OrderRepository ordersRepo,
        OrderItemRepository orderItemRepo
    ) {
        this.orderRepo = ordersRepo;
        this.orderItemRepo = orderItemRepo;
    }

    
    public AdminAnalyticsResponse getAnalytics() {

        AdminAnalyticsResponse res = new AdminAnalyticsResponse();

        res.setTotalOrders(orderRepo.countOrders());
        res.setTotalRevenue(orderRepo.totalRevenue());

        // status breakdown
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : orderRepo.countByStatus()) {
            statusMap.put(row[0].toString(), (Long) row[1]);
        }
        res.setOrdersByStatus(statusMap);

        // top products
        List<TopProductDto> top = orderItemRepo.findTopSellingProducts()
            .stream()
            .map(r -> new TopProductDto(
                (String) r[0],
                (Long) r[1]
            ))
            .toList();

        res.setTopProducts(top);

        return res;
    }
}

