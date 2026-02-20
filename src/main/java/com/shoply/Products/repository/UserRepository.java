package com.shoply.Products.repository;

import com.shoply.Products.Model.Status;
import com.shoply.Products.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<Users, String> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByUsernameOrEmail(String username, String email);

    List<Users> findAllByStatus(Status status);
}
