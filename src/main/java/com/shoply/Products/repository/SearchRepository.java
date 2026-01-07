package com.shoply.Products.repository;

import com.shoply.Products.Model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository {
    List<Product> searchProducts(String productName);
}
