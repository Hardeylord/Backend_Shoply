package com.shoply.Products.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String signature;
    private final long EXPIRATION_DATE=1000*60*60;

//    public JWTService() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator= KeyGenerator.getInstance("HmacSHA256");
//        SecretKey secretKey= keyGenerator.generateKey();
//        signature= Encoders.BASE64URL.encode(secretKey.getEncoded());
//    }


    public String generateJWTToken(String username, String role) {
        Map<String, String> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_DATE))
                .signWith(genKey())
                .compact();
    }

    private Key genKey() {
        byte[] keyByte = Decoders.BASE64URL.decode(signature);
//        System.out.println(signature+ " <- This is the signature");
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String extractUsername(String token) throws NoSuchAlgorithmException {
       return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) throws NoSuchAlgorithmException {
        return Jwts.parser()
                .verifyWith((SecretKey) genKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken( UserDetails service,String token,String username) throws NoSuchAlgorithmException {
        return  username.equals(service.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
