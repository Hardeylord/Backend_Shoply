package com.shoply.Products.repository;

import com.shoply.Products.Model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Orders, String> {
}
