package com.app.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.Ecommerce.model.Orders;

@Repository
public interface OrderRepository  extends JpaRepository<Orders, Integer> {

	List<Orders> findByBuyerEmailIdOrderByTimestampDesc(String emailId);

	Optional<Orders> findByOrderIdAndBuyerEmailId(int orderId, String emailId);

	Optional<Orders> findBySessionId(String sessionId);

	@Query("""
		    SELECT DISTINCT o
		    FROM Orders o
		    JOIN o.items i
		    WHERE i.product.seller.emailId = :email
		    AND o.status IN ('PENDING', 'SHIPPED', 'DELIVERED')
		    ORDER BY o.timestamp DESC
		""")
		List<Orders> findSellerOrders(@Param("email") String email);


	@Query("""
		    SELECT o FROM Orders o
		    JOIN FETCH o.items i
		    JOIN FETCH i.product
		    WHERE o.orderId = :id
		    AND o.buyer.emailId = :email
		""")
		Optional<Orders> findOrderWithItemsAndProducts(
		    @Param("id") int id,
		    @Param("email") String email
		);
	
	@Query("""
		    SELECT o
		    FROM Orders o
		    LEFT JOIN FETCH o.items i
		    LEFT JOIN FETCH i.product
		    WHERE o.orderId = :id AND o.buyer.emailId = :email
		""")
		Optional<Orders> findOrderWithItems(
		    @Param("id") int id,
		    @Param("email") String email
		);


    @Query("SELECT COUNT(o) FROM Orders o")
    long countOrders();


    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Orders o")
    double totalRevenue();

    @Query("""
        SELECT o.status, COUNT(o)
        FROM Orders o
        GROUP BY o.status
    """)
    List<Object[]> countByStatus();
	

}