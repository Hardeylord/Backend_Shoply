package com.shoply.Products.Controller;

import com.shoply.Products.Model.Users;
import com.shoply.Products.services.TokenService;
import com.shoply.Products.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Users user){

        return userService.signup(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user){

        try{
            return ResponseEntity.ok(userService.verifyUser(user));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage()+ " USER NOT FOUND");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("signup/admin")
    public ResponseEntity<Users> adminSignUp(@RequestBody Users users){
        return userService.adminSignUp(users);
    }

    @PostMapping("/accountUpgrade")
    public void accountUpgrade(@RequestBody Users users){
        System.out.println("i got here"+users.getEmail());
        tokenService.accountUpgrade(users);
    }

    @PostMapping("/validateToken")
    public void upgradeTokenVerify(@RequestParam("token") String token){
        tokenService.upgradeTokenVerification(token);
    }

}
