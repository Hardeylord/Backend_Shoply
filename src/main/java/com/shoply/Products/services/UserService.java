package com.shoply.Products.services;

import com.shoply.Products.Model.Status;
import com.shoply.Products.Model.Token;
import com.shoply.Products.Model.Users;
import com.shoply.Products.repository.TokenRepository;
import com.shoply.Products.repository.UserRepository;
import com.shoply.Products.rolePermission.ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;



    public ResponseEntity<?> signup(Users user) {
        Optional<Users> existingUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername());
        if(existingUser.isPresent()){
            return new ResponseEntity<>(existingUser, HttpStatus.BAD_REQUEST);
        } else {
            user.setRole(ROLE.USER);
            user.setEnabled(true);
            user.setStatus(Status.ONLINE);
            user.setPassword(encoder.encode(user.getPassword()));
            return ResponseEntity.ok(userRepository.save(user));
        }
    }
    @Autowired
    JWTService jwtService;

    public String verifyUser(Users user) {
        Authentication am = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword() ));
        if (am.isAuthenticated()){
            Users userClaim=userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

            return jwtService.generateJWTToken(userClaim.getUsername(), userClaim.getRole().name());
        }
        throw new RuntimeException("USER NOT FOUND");
    }

    public ResponseEntity<Users> adminSignUp(Users users) {
        Optional<Users> alreadyRegistered = userRepository.findByUsernameOrEmail(users.getUsername(), users.getUsername());

        if (alreadyRegistered.isPresent()){
            return new ResponseEntity<>(users, HttpStatus.BAD_REQUEST);
        } else {
            users.setRole(ROLE.ADMIN);
            users.setEnabled(true);
            users.setPassword(encoder.encode(users.getPassword()));
            return ResponseEntity.ok(userRepository.save(users));
        }
    }
}
