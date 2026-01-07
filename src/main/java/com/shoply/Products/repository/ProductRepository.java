package com.shoply.Products.repository;

import com.shoply.Products.Model.Product;
import com.shoply.Products.interfaces.LatestProductInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ProductRepository extends MongoRepository<Product, String> {

    void deleteByName(String name);
    Optional<Product> findByName(String name);
    List<LatestProductInterface> findAllBy();
}
