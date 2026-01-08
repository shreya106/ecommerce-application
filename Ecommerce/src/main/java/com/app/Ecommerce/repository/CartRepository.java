package com.app.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Ecommerce.model.Cart;
import com.app.Ecommerce.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	Optional<Cart> findByUser(User user);

}
