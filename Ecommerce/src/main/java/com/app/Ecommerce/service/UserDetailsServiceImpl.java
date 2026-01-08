package com.app.Ecommerce.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 User user = userRepository.findByEmailId(email)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		 	String role = "ROLE_"+user.getRole().name();
		 	
	        return org.springframework.security.core.userdetails.User
	                .withUsername(user.getEmailId())
	                .password(user.getPassword())
	                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
	                .build();
	}
	
	
	
	

}
