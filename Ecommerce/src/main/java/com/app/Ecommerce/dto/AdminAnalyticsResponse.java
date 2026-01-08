package com.app.Ecommerce.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AdminAnalyticsResponse {
    private long totalOrders;
    private double totalRevenue;
    private Map<String, Long> ordersByStatus;
    private List<TopProductDto> topProducts;
}