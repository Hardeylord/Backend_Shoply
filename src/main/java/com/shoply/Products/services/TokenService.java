package com.shoply.Products.services;

import com.shoply.Products.Model.Token;
import com.shoply.Products.Model.Users;
import com.shoply.Products.repository.TokenRepository;
import com.shoply.Products.repository.UserRepository;
import com.shoply.Products.rolePermission.ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    EmailService emailService;
    public void accountUpgrade(Users users) {
//        check if user exist
        Users userData = userRepository.findByUsernameOrEmail(users.getEmail(), users.getEmail()).orElseThrow(()-> new IllegalStateException("INVALID CREDENTIALS1"));
        System.out.println(userData.getEmail());
//        Users user=userRepository.findByEmail(userData.getEmail()).orElseThrow(()-> new IllegalStateException("INVALID CREDENTIALS"));
//        generate upgrade token
        Token upgradeToken= new Token(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userData
        );
        tokenRepository.save(upgradeToken);
        emailService.send(userData.getEmail(), upgradeToken.getToken());
    }

    public void upgradeTokenVerification(String token) {
        Token token1 = tokenRepository.findByToken(token).orElseThrow(()-> new IllegalStateException("Invalid Token"));
        if (token1 == null){
            throw new IllegalStateException("Invalid Token");
        }
        if (token1.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Expired Token");
        }

        token1.setConfirmedAt(LocalDateTime.now());
//        change role and enable account
        upgradeAccount(token1.getUser());

        tokenRepository.save(token1);
    }

    private void upgradeAccount(Users user) {
        user.setRole(ROLE.EDITOR);

        userRepository.save(user);
    }
}
