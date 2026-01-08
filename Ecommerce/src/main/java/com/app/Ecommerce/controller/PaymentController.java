package com.app.Ecommerce.controller;



import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.service.OrderService;
import com.app.Ecommerce.service.OrderServiceImpl;
import com.app.Ecommerce.service.PaymentServiceImpl;
import com.stripe.exception.StripeException;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	
	private final PaymentServiceImpl paymentServiceImpl;
	private final OrderServiceImpl orderServiceImpl;
	
	public PaymentController(PaymentServiceImpl paymentServiceImpl, OrderServiceImpl orderServiceImpl) {
		this.paymentServiceImpl = paymentServiceImpl;
		this.orderServiceImpl = orderServiceImpl;
	}
	
	private String email() { return SecurityContextHolder.getContext().getAuthentication().getName(); }
	
	@PostMapping("/create-checkout-session")
	public ResponseEntity<Map<String, Object>> createCheckoutSession() throws StripeException{
	
	    return ResponseEntity.ok(paymentServiceImpl.createCheckoutSession(email()));
		
		
	}
	
	 @GetMapping("/success")
	    public String getSuccess(){
	        return "payment successful";
	    }

	    @GetMapping("/cancel")
	    public String cancel(){
	        return "payment canceled";
	    }

}
