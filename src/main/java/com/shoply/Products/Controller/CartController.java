package com.shoply.Products.Controller;

import com.shoply.Products.Model.CartDto;
import com.shoply.Products.Model.Users;
import com.shoply.Products.repository.CartRepository;
import com.shoply.Products.repository.UserRepository;
import com.shoply.Products.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestBody CartDto cartDto) {
       return ResponseEntity.ok(cartService.addToCart(cartDto));
    }

    @GetMapping("/cartItems")
    public ResponseEntity<?> getUserCart() {
        String userInfo = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByUsernameOrEmail(userInfo, userInfo).orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND!!!"));
        String id =user.getId();
        return ResponseEntity.ok(cartRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Cart not found")));
    }

    @DeleteMapping("/remove{id}")
    public ResponseEntity<?> deleteFromCart(@PathVariable("id") String id){
       return ResponseEntity.ok(cartService.deleteItem(id));
    }

}
