package com.app.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Ecommerce.model.Cart;
import com.app.Ecommerce.model.CartItem;
import com.app.Ecommerce.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
