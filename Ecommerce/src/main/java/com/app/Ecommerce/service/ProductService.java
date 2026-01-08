package com.app.Ecommerce.service;

import java.util.List;

import com.app.Ecommerce.dto.SellerRequest;
import com.app.Ecommerce.model.Product;

public interface ProductService {
  public Product updateProduct(int id, SellerRequest sellerRequest,String imageUrl);
  public void deleteProduct(int id);
  public Product createProduct(SellerRequest sellerRequest, String imageUrl);
  public Boolean updateStock(int id, int stock);
  public List<Product> getSellerProducts();
  public void restoreProduct(int id);
}
