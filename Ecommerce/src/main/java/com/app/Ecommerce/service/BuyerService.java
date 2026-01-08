package com.app.Ecommerce.service;

import java.util.List;

import com.app.Ecommerce.dto.ProductResponse;
import com.app.Ecommerce.model.Product;

public interface BuyerService {

	public List<ProductResponse> getAllActiveProducts();
	public Product getProductById(int id);
	public List<ProductResponse> searchProducts(String name, String category);
	
}
