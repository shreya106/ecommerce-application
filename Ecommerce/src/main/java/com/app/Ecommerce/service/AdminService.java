package com.app.Ecommerce.service;


import java.util.List;
import java.util.Map;

import com.app.Ecommerce.dto.AdminOrderResponse;
import com.app.Ecommerce.dto.AdminProductResponse;
import com.app.Ecommerce.dto.AdminUserResponse;
import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;

	public interface AdminService {
	    List<AdminUserResponse> getAllUsers();
	    void deleteUser(int id);
	    List<AdminProductResponse> getAllProducts();
	    void toggleProductActive(int id);
	    List<AdminOrderResponse> getAllOrders();
	    void updateOrderStatus(int id, String status);
	    Map<String, Object> getDashboardStats();
	    void toggleUserActive(int id);
	}

