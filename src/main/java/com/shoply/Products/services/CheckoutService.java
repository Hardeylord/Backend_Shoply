package com.shoply.Products.services;

import com.shoply.Products.Model.CartItems;
import com.shoply.Products.Model.Checkout_session;
import com.shoply.Products.Model.Product;
import com.shoply.Products.Model.Users;
import com.shoply.Products.repository.CartRepository;
import com.shoply.Products.repository.CheckOutRepository;
import com.shoply.Products.repository.ProductRepository;
import com.shoply.Products.repository.UserRepository;
import com.shoply.Products.rolePermission.CHECKOUT_STATUS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckOutRepository checkOutRepository;

    public Checkout_session validateCartItems() {

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Users userClaim = userRepository.findByUsernameOrEmail(user, user).orElseThrow(()-> new RuntimeException("USER NOT FOUND"));
        List<CartItems> products = cartRepository.findByUserId(userClaim.getId()).stream().flatMap(cart -> cart.getCartItems().stream()).toList();
        double sum = products.stream().mapToDouble(CartItems::getPrice).sum();

        if (Double.compare(sum, actualProductPrice(products)) != 0){
            throw new RuntimeException("CART PRICE MISMATCH");
        }

        Optional<Checkout_session> idempotent= checkOutRepository.findByUserIdAndStatusAndExpiresAtAfter(userClaim, CHECKOUT_STATUS.PENDING, Instant.now());
        if (idempotent.isPresent()) {
            return idempotent.get();
        }

        Checkout_session checkoutSession = new Checkout_session();
            checkoutSession.setUserId(userClaim);
            checkoutSession.setItems(products);
            checkoutSession.setSub_total(sum);
            checkoutSession.setCurrency("usd");
            checkoutSession.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
            checkoutSession.setCheckoutStatus(CHECKOUT_STATUS.PENDING);

        checkOutRepository.save(checkoutSession);

            return checkoutSession;
    }


    //    SUM PRODUCT PRICE FROM DB
    private Double actualProductPrice(List<CartItems> products) {

        Set<String> productIdS = products.stream().map(CartItems::getProductId).collect(Collectors.toSet());
          return   productRepository.findAllById(productIdS)
                    .stream().mapToDouble(Product::getPrice).sum();
    }
}
