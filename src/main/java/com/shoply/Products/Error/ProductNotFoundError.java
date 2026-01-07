package com.shoply.Products.Error;

public class ProductNotFoundError extends RuntimeException{

    public ProductNotFoundError(String message){
        super(message);
    }
}
