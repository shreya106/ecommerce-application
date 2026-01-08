package com.app.Ecommerce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.dto.AuthRequest;
import com.app.Ecommerce.dto.AuthResponse;
import com.app.Ecommerce.dto.RegisterRequest;
import com.app.Ecommerce.enums.Role;
import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.UserRepository;
import com.app.Ecommerce.security.JwtUtils;
import com.app.Ecommerce.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRespository;
	
	@Autowired
	private PasswordEncoder passswordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
    private EmailService emailService;
	
	
	@PostMapping("/register")
	private ResponseEntity<String> register(@Valid @RequestBody RegisterRequest register){
		User user = new User();
		
		user.setName(register.getName());
		user.setAddress(register.getAddress());
		user.setEmailId(register.getEmail());
		user.setPassword(passswordEncoder.encode(register.getPassword()));
		user.setRole(Role.valueOf(register.getRole().toUpperCase()));
		user.setCountry(register.getCountry());
		user.setLocation(register.getLocation());
		user.setPhoneNumber(register.getPhoneNumber());
		userRespository.save(user);
		
		emailService.sendMail(
		        user.getEmailId(),
		        "Welcome to Cartify!",
		        "Hi " + user.getName() + ",\n\nWelcome to our platform!"
		    );
		
	return ResponseEntity.ok("User Registered Successfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request) {

	    try {
	        Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                request.getEmail(),
	                request.getPassword()
	            )
	        );

	        User user = userRespository.findByEmailId(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	        String token = jwtUtils.generateToken(
	            user.getEmailId(),
	            user.getRole().name()
	        );

	        return ResponseEntity.ok(new AuthResponse(token));

	    } catch (BadCredentialsException ex) {
	        return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of("message", "Incorrect email or password"));
	    }
	}

	
}
