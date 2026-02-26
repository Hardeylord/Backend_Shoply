package com.shoply.Products.repository;

import com.shoply.Products.Model.Checkout_session;
import com.shoply.Products.Model.Users;
import com.shoply.Products.rolePermission.CHECKOUT_STATUS;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CheckOutRepository extends MongoRepository<Checkout_session, String> {

    Optional<Checkout_session> findByIdempotencyKey(String idempotencyKey);
}
