package com.shoply.Products.services;

import com.shoply.Products.Model.RefreshToken;
import com.shoply.Products.Model.Status;
import com.shoply.Products.Model.Users;
import com.shoply.Products.Response.JWTResponse;
import com.shoply.Products.repository.RefreshTokenRepository;
import com.shoply.Products.repository.UserRepository;
import com.shoply.Products.rolePermission.ROLE;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

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

    public JWTResponse verifyUser(Users user) {
        Authentication am = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword() ));
        if (am.isAuthenticated()){
            Users userClaim=userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
            System.out.println(userClaim.getRole().name());
            userClaim.setStatus(Status.ONLINE);
            userRepository.save(userClaim);
            RefreshToken refreshToken=refreshTokenService.createRefreshToken(userClaim);
            return new JWTResponse(jwtService.generateJWTToken(userClaim.getUsername(), userClaim.getRole().name()), refreshToken.getToken());
//            return jwtService.generateJWTToken(userClaim.getUsername(), userClaim.getRole().name());
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

    public void logoutUser(HttpServletRequest httpServletRequest, String rToken) {
//        System.out.println("logged out...");
        String refreshToken = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null){
          refreshToken =  Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .findFirst()
                    .map(Cookie::getValue).orElse(null);
        }
        if (refreshToken == null && rToken != null && !rToken.isBlank()) {
            refreshToken = rToken;
        }

        if (refreshToken != null) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }
    }
}
