package com.app.Ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.Ecommerce.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Query("""
	        SELECT p.name, SUM(oi.quantity)
	        FROM OrderItem oi
	        JOIN oi.product p
	        GROUP BY p.name
	        ORDER BY SUM(oi.quantity) DESC
	    """)
	    List<Object[]> findTopSellingProducts();
}
