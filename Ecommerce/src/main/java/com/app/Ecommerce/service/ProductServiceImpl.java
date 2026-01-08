package com.app.Ecommerce.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.Ecommerce.dto.SellerProductResponse;
import com.app.Ecommerce.dto.SellerRequest;
import com.app.Ecommerce.exception.ResourceNotFoundException;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.ProductRepository;
import com.app.Ecommerce.repository.UserRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final StockNotificationService stockNotificationService;
		
	public ProductServiceImpl(ProductRepository productRepository,UserRepository userRepository, StockNotificationService stockNotificationService) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.stockNotificationService = stockNotificationService;
	}
	
	@Override
	public Product updateProduct(int id, SellerRequest sellerRequest, String imageUrl) {
	  
					
			Product product = productRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName();
			User loggedInUser = userRepository.findByEmailId(email)
		            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email: " + email));

			if(product.getSeller().getId() != loggedInUser.getId()) {
				throw new RuntimeException("You are not authorized to update this product!");
			}
			
			
			product.setName(sellerRequest.getName());
			product.setCategories(sellerRequest.getCategories());
			product.setDescription(sellerRequest.getDescription());
			
			if(sellerRequest.getPrice()<=0) {
			throw new RuntimeException("Product price must be greater than 0");
			}
			else {
				product.setPrice(sellerRequest.getPrice());
			}
			if(sellerRequest.getStock()<0) {
				throw new RuntimeException("Stock price must be positive");
				}
				else {
					product.setStock(sellerRequest.getStock());
				}
			
			if (imageUrl != null && !imageUrl.isBlank()) {
		        product.setImageUrl(imageUrl);
		    }
				
		return productRepository.save(product);
		
	}

	@Override
	public void deleteProduct(int id) {
		Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User loggedInUser = userRepository.findByEmailId(email)
	            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email: " + email));

		if(product.getSeller().getId() != loggedInUser.getId()) {
			throw new RuntimeException("You are not authorized to delete this product!");
		}
		
		product.setIsActive(false);
		productRepository.save(product);
		
	
	}

	@Override
	public Product createProduct(SellerRequest sellerRequest, String imageUrl) {

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName();

	    User seller = userRepository.findByEmailId(email)
	        .orElseThrow(() -> new ResourceNotFoundException(
	            "Seller not found with email: " + email));

	    Product prd = new Product();

	    prd.setName(sellerRequest.getName());
	    prd.setCategories(sellerRequest.getCategories());
	    prd.setDescription(sellerRequest.getDescription());

	    if (sellerRequest.getPrice() <= 0) {
	        throw new IllegalArgumentException("Product price must be greater than 0");
	    }
	    prd.setPrice(sellerRequest.getPrice());

	    if (sellerRequest.getStock() < 0) {
	        throw new IllegalArgumentException("Stock cannot be negative");
	    }
	    prd.setStock(sellerRequest.getStock());

	    prd.setSeller(seller);
	    prd.setIsActive(true);
	    prd.setImageUrl(imageUrl);

	    return productRepository.save(prd);
	}

	public SellerProductResponse getSellerProductById(int id) {
	    Product p = productRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

	    SellerProductResponse dto = new SellerProductResponse();
	    dto.setId(p.getProdId());
	    dto.setName(p.getName());
	    dto.setPrice(p.getPrice());
	    dto.setCategories(p.getCategories());
	    dto.setStock(p.getStock());
	    dto.setDescription(p.getDescription());   // ✅ ADD THIS
	    dto.setImageUrl(p.getImageUrl());
	    dto.setActive(p.getIsActive());
	    return dto;
	}

	@Override
	public Boolean updateStock(int id, int stock) {
		if(stock <0) {
		throw new IllegalArgumentException("Stock cannot be negative");
		}
		Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User loggedInUser = userRepository.findByEmailId(email)
	            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email: " + email));

		if(product.getSeller().getId() != loggedInUser.getId()) {
			throw new RuntimeException("You are not authorized to update this product!");
		}
	
		
			product.setStock(stock);
			productRepository.save(product);
			
			stockNotificationService.notifyStockChange(id, stock);
		
		return true;
		
	}
	

	
	@Override
	public List<Product> getSellerProducts() {
	    String email = SecurityContextHolder
	        .getContext()
	        .getAuthentication()
	        .getName();

	    User seller = userRepository.findByEmailId(email)
	        .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

	    return productRepository.findBySeller(seller);
	}
	
	public List<SellerProductResponse> getProductsBySeller(String email) {

	    User seller = userRepository.findByEmailId(email)
	        .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

	    return productRepository.findBySeller(seller)
	        .stream()
	        .map(p -> {
	            SellerProductResponse dto = new SellerProductResponse();
	            dto.setId(p.getProdId());
	            dto.setName(p.getName());
	            dto.setCategories(p.getCategories());
	            dto.setPrice(p.getPrice());
	            dto.setStock(p.getStock());
	            dto.setActive(p.getIsActive());
	            return dto;
	        })
	        .toList();
	}
	
	@Override
	public void restoreProduct(int id) {

	    Product product = productRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException(
	            "Product not found with id: " + id));

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName();

	    User loggedInUser = userRepository.findByEmailId(email)
	        .orElseThrow(() -> new ResourceNotFoundException(
	            "Seller not found with email: " + email));

	    // Authorization check
	    if (product.getSeller().getId() != loggedInUser.getId()) {
	        throw new RuntimeException("You are not authorized to restore this product!");
	    }

	    // Restore
	    product.setIsActive(true);
	    productRepository.save(product);
	    System.out.println("Product restored: " + product.getProdId());
	}

	
	



}
