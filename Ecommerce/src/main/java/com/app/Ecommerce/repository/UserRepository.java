package com.app.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
		Optional<User> findByEmailId(String emailId);
		
}
