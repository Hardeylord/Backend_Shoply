package com.shoply.Products.services;

import com.shoply.Products.Model.*;
import com.shoply.Products.repository.CartRepository;
import com.shoply.Products.repository.ProductRepository;
import com.shoply.Products.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;

    public Cart addToCart(CartDto cartDto) {

        String userInfo = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(userInfo);
//        System.out.println(cartDto.getProdId());
        Users user = userRepository.findByUsernameOrEmail(userInfo, userInfo).orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND!!!"));
        String id =user.getId();
//        System.out.println(id);

//        find or create cart
        Cart cart = cartRepository.findByUserId(id)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(id);
                    return cartRepository.save(newCart);
                });

        // Check if product already exists
        Optional<CartItems> existing = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(cartDto.getProdId()))
                .findFirst();

        if (existing.isPresent()) {
            return null;
        } else {
            CartItems newItem = new CartItems();
//            store single product in cartItems
            Product pFeatures=productRepository.findById(cartDto.getProdId()).orElseThrow(()-> new RuntimeException("OUT OF PRODUCT"));
//            store product image,name,price,quantity
            newItem.setProductId(cartDto.getProdId());
            newItem.setProductName(pFeatures.getName());
            newItem.setPrice(pFeatures.getPrice());
            newItem.setImageUrl(pFeatures.getImage());

            cart.getCartItems().add(newItem);
        }

        return cartRepository.save(cart);


//        Product product = productRepository.findById(cartDto.getProdId()).orElseThrow(()-> new ProductNotFoundError("PRODUCT NOT FOUND"));
//        checks if user already has a cart
//        Cart cart = cartRepository.findByUser(user).orElseGet(()-> {
//            Cart newCart = new Cart();
//            newCart.setUser(user);
//            return cartRepository.save(newCart);
//        });
//
//        Optional<CartItems> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
//
//        if (existingItem.isPresent()){
//            return null;
//        } else {
//            CartItems addToCart = new CartItems();
//            addToCart.setCart(cart);
//            addToCart.setProduct(product);
//
//          return cartItemRepository.save(addToCart);
//        }


        }

    public Cart deleteItem(String id) {
        String userInfo = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findByUsername(userInfo).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        Cart cartItems = cartRepository.findByUserId(users.getId()).orElseThrow(()-> new RuntimeException("Empty Cart"));

        Optional<CartItems> existing = cartItems.getCartItems().stream()
                .filter(item -> item.getProductId().equals(id))
                .findFirst();

        existing.ifPresent(items -> cartItems.getCartItems().remove(items));

        return cartRepository.save(cartItems);
    }
}
