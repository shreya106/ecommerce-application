	package com.app.Ecommerce.controller;
	
	import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.Ecommerce.dto.SellerProductResponse;
import com.app.Ecommerce.dto.SellerRequest;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.repository.ProductRepository;
import com.app.Ecommerce.service.ImageUploadServiceImpl;
import com.app.Ecommerce.service.ProductServiceImpl;

import jakarta.validation.Valid;
	
	
		@RestController
		@RequestMapping("/api/seller/products")
		public class SellerController {
		
		ProductRepository productRepository;
		ProductServiceImpl productService;
		ImageUploadServiceImpl imageUploadService;
		
		public SellerController(ProductServiceImpl productService, ImageUploadServiceImpl imageUploadService) {
			this.productService = productService;
			this.imageUploadService = imageUploadService;
		}
			
		@GetMapping
		public ResponseEntity<List<SellerProductResponse>> getSellerProducts() {
		    String email = SecurityContextHolder
		        .getContext()
		        .getAuthentication()
		        .getName();

		    return ResponseEntity.ok(productService.getProductsBySeller(email));
		}
		@GetMapping("/{id}")
		public ResponseEntity<SellerProductResponse> getProductById(@PathVariable int id) {
		    return ResponseEntity.ok(productService.getSellerProductById(id));
		}
		
		@PostMapping(consumes = "multipart/form-data")
		public ResponseEntity<Product> createProduct(
		        @RequestParam String name,
		        @RequestParam double price,
		        @RequestParam int stock,
		        @RequestParam String categories,
		        @RequestParam String description,
		        @RequestParam MultipartFile image
		) {

		    String imageUrl = imageUploadService.uploadImage(image);

		    SellerRequest sellerRequest = new SellerRequest();
		    sellerRequest.setName(name);
		    sellerRequest.setPrice(price);
		    sellerRequest.setStock(stock);
		    sellerRequest.setCategories(categories);
		    sellerRequest.setDescription(description);

		    Product product = productService.createProduct(sellerRequest, imageUrl);
		    return ResponseEntity.status(201).body(product);
		}
		
		@PutMapping(value = "/{id}", consumes = "multipart/form-data")
		public ResponseEntity<SellerProductResponse> updateProduct(
		        @PathVariable int id,
		        @RequestParam String name,
		        @RequestParam double price,
		        @RequestParam int stock,
		        @RequestParam String categories,
		        @RequestParam String description,
		        @RequestParam(required = false) MultipartFile image
		) {

		    String imageUrl = null;
		    if (image != null && !image.isEmpty()) {
		        imageUrl = imageUploadService.uploadImage(image);
		    }

		    SellerRequest sellerRequest = new SellerRequest();
		    sellerRequest.setName(name);
		    sellerRequest.setPrice(price);
		    sellerRequest.setStock(stock);
		    sellerRequest.setCategories(categories);
		    sellerRequest.setDescription(description);

		    Product product = productService.updateProduct(id, sellerRequest, imageUrl);
		    SellerProductResponse dto = new SellerProductResponse();
		    dto.setId(product.getProdId());
		    dto.setName(product.getName());
		    dto.setCategories(product.getCategories());
		    dto.setPrice(product.getPrice());
		    dto.setStock(product.getStock());
		    dto.setActive(product.getIsActive());
		    dto.setImageUrl(product.getImageUrl());
		    return ResponseEntity.ok(dto);
		}

			
			
			
			@DeleteMapping("/{id}")
			public ResponseEntity<Void> deleteProduct(@PathVariable int id){
				productService.deleteProduct(id);
				return ResponseEntity.noContent().build();
				
			}
			
			@PatchMapping("/{id}/stock")
			public ResponseEntity<Boolean> updateStock(@PathVariable int id, @RequestParam int stock){
				
				Boolean val = productService.updateStock(id,stock);
				
				return ResponseEntity.ok(val);
			}
			@PatchMapping("/{id}/restore")
			public ResponseEntity<Void> restoreProduct(@PathVariable int id) {
			    productService.restoreProduct(id);
			    return ResponseEntity.ok().build();
			}
			
		}
