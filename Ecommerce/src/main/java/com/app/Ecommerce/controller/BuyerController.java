package com.app.Ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Ecommerce.dto.ProductResponse;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.service.BuyerServiceImpl;

@RestController
@RequestMapping("/api/buyer/product")
public class BuyerController {

	private final BuyerServiceImpl buyerService;
	
	public BuyerController(BuyerServiceImpl buyerSerivce) {
		this.buyerService = buyerSerivce;
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		return ResponseEntity.ok(buyerService.getAllActiveProducts());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable int id){
		return ResponseEntity.ok(buyerService.getProductById(id));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam(required=false) String name,@RequestParam(required=false) String category){
		
		return ResponseEntity.ok(buyerService.searchProducts(name, category));
	}
}
