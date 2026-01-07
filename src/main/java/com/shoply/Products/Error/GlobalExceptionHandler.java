package com.shoply.Products.Error;

import com.shoply.Products.Response.MyErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundError.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundError error){
        MyErrorResponse errorResponse= new MyErrorResponse(LocalDateTime.now(), error.getMessage(), "Product Not Found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
