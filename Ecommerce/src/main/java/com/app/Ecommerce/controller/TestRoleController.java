package com.app.Ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRoleController {
	 @GetMapping("/buyer/hello")
	    public String buyerHello() {
	        return "Hello Buyer, you are authorized!";
	    }

	    @GetMapping("/seller/hello")
	    public String sellerHello() {
	        return "Hello Seller, you are authorized!";
	    }

	    @GetMapping("/admin/hello")
	    public String adminHello() {
	        return "Hello Admin, you are authorized!";
	    }
}
