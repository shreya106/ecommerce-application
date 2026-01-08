package com.app.Ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.Ecommerce.dto.ProductResponse;
import com.app.Ecommerce.exception.ResourceNotFoundException;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.repository.ProductRepository;

@Service
public class BuyerServiceImpl implements BuyerService {

    private final ProductRepository productRepository;

    public BuyerServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue()
                .stream()
                .map(p -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setId(p.getProdId());
                    dto.setName(p.getName());
                    dto.setDescription(p.getDescription());
                    dto.setPrice(p.getPrice());
                    dto.setStock(p.getStock());
                    dto.setCategory(p.getCategories()); 
                    dto.setImageUrl(p.getImageUrl());
                    return dto;
                })
                .toList();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id)
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + id));
    }

    @Override
    public List<ProductResponse> searchProducts(String name, String category) {
        List<Product> products;

        if (name != null && category != null) {
            products = productRepository
                    .findByNameContainingIgnoreCaseAndCategoriesContainingIgnoreCaseAndIsActiveTrue(name, category);
        } else if (name != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
        } else if (category != null) {
            products = productRepository.findByCategoriesContainingIgnoreCaseAndIsActiveTrue(category);
        } else {
            products = productRepository.findByIsActiveTrue();
        }

        return products.stream()
                .map(p -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setId(p.getProdId());
                    dto.setName(p.getName());
                    dto.setDescription(p.getDescription());
                    dto.setPrice(p.getPrice());
                    dto.setStock(p.getStock());
                    dto.setCategory(p.getCategories());
                    dto.setImageUrl(p.getImageUrl());
                    return dto;
                })
                .toList();
    }
}
