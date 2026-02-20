package com.shoply.Products.repository;

import com.shoply.Products.Model.Cart;
import com.shoply.Products.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByUserId(String user);

    Cart deleteCartItemsByUserId(String userId);
}
