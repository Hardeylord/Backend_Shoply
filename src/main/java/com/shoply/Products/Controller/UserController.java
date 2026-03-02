package com.shoply.Products.Controller;

import com.shoply.Products.Model.RTokenDto;
import com.shoply.Products.Model.RefreshToken;
import com.shoply.Products.Model.Users;
import com.shoply.Products.Response.JWTResponse;
import com.shoply.Products.services.JWTService;
import com.shoply.Products.services.RefreshTokenService;
import com.shoply.Products.services.TokenService;
import com.shoply.Products.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JWTService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Users user){

        return userService.signup(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user){
        JWTResponse tokens = userService.verifyUser(user);
//        System.out.println(tokens.getRefreshToken());
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        try{
            return ResponseEntity.ok().
                    header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(new JWTResponse(tokens.getAccessToken(), tokens.getRefreshToken()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage()+ " USER NOT FOUND");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest, @RequestBody(required = false) RTokenDto rToken){
        String refreshToken=null;
//        refreshToken= Arrays.stream(httpServletRequest.getCookies())
//                .filter(cookie -> cookie.getName().equals("refreshToken"))
//                .findFirst()
//                .map(Cookie::getValue)
//                .orElse(null);
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
//        System.out.println("rToken is: ..."+refreshToken);
        if (refreshToken == null){
            if (rToken.getrToken() != null) {
                refreshToken=rToken.getrToken();
            } else {
                throw new RuntimeException("NO REFRESH TOKEN FOUND");
            }
        }
        RefreshToken tokenExist = refreshTokenService.isTokenExist(refreshToken).orElse(null);
        if (tokenExist == null){
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(0)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                    .build();
        }
        RefreshToken tokenExpired = refreshTokenService.isTokenExpired(tokenExist);
        return ResponseEntity.ok(jwtService.generateJWTToken(tokenExpired.getUser().getUsername(),tokenExpired.getUser().getRole().name()));
    }

    @PostMapping("/auth/logout")
    public void logoutUser(HttpServletRequest httpServletRequest, @RequestBody(required = false) RTokenDto rToken){
        userService.logoutUser(httpServletRequest, rToken.getrToken());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("signup/admin")
    public ResponseEntity<Users> adminSignUp(@RequestBody Users users){
        return userService.adminSignUp(users);
    }

    @PostMapping("/accountUpgrade")
    public void accountUpgrade(@RequestBody Users users){
        tokenService.accountUpgrade(users);
    }

    @PostMapping("/validateToken")
    public void upgradeTokenVerify(@RequestParam("token") String token){
        tokenService.upgradeTokenVerification(token);
    }

}
