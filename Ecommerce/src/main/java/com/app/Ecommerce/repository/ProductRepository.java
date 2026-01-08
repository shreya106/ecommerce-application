package com.app.Ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	List<Product> findByIsActiveTrue();
	List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
	List<Product> findByCategoriesContainingIgnoreCaseAndIsActiveTrue(String category);
	List<Product> findByNameContainingIgnoreCaseAndCategoriesContainingIgnoreCaseAndIsActiveTrue(String name, String category);
	List<Product> findBySeller(User seller);
	List<Product> findBySellerAndIsActiveTrue(User seller);

}
