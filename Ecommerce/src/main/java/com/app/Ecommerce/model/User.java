package com.app.Ecommerce.model;

import java.util.List;

import com.app.Ecommerce.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role = Role.BUYER;
	
	
	@Column(name="email_id")
	private String emailId;
	
	private String password;
	
	private String address;
	
	private String location;
	
	private String country;
	
	private String phoneNumber;
	
	@Column(name="is_verified")
	private boolean isVerified = false;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
    private Cart cart;
	
	
	@OneToMany(mappedBy = "seller")
	@JsonIgnore
	private List<Product> products;

	@OneToMany(mappedBy = "buyer")
	@JsonIgnore
	private List<Orders> orders;
	
	@Column(name = "is_active")
	private Boolean isActive = true;

	
	
}
