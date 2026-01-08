package com.app.Ecommerce.service;

import java.util.Map;

import com.stripe.exception.StripeException;

public interface PaymentService {
	public Map<String, Object> createCheckoutSession(String email) throws StripeException;
}
