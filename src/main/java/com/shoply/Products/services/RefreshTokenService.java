package com.shoply.Products.services;

import com.shoply.Products.Model.RefreshToken;
import com.shoply.Products.Model.Users;
import com.shoply.Products.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(Users userClaim){
        RefreshToken refreshToken=new RefreshToken(UUID.randomUUID().toString(),
                LocalDateTime.now().plusHours(10), userClaim);
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> isTokenExist(String refreshToken){
      return refreshTokenRepository.findByToken(refreshToken);
    }

    public RefreshToken isTokenExpired(RefreshToken refreshToken){
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(0)
                    .build();
        }
        return refreshToken;
    }
}
