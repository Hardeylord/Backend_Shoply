package com.shoply.Products.filters;

import com.shoply.Products.services.CustomUserDetailsService;
import com.shoply.Products.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (authHeader !=null && authHeader.contains("Bearer ")){
           token= authHeader.substring(7);
//           System.out.println(token+" <- This is the token");
           try{
               username=jwtService.extractUsername(token);
           } catch (NoSuchAlgorithmException e) {
               throw new RuntimeException(e);
           }

        }

        if (username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails service = userDetailsService.loadUserByUsername(username);
            try {
//                System.out.println("tryinngggggg");
                if (jwtService.validateToken(service, token, username)){
//                    System.out.println(3444);
                    UsernamePasswordAuthenticationToken uNamePwdToken = new UsernamePasswordAuthenticationToken(service, null, service.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(uNamePwdToken);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        filterChain.doFilter(request, response);
    }
}
