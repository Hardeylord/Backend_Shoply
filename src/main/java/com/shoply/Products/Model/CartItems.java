package com.shoply.Products.Model;

import java.util.List;

public class CartItems {

//    private Product product;
    private String productId;
    private String productName;
    private double price;
    private List imageUrl;


    public CartItems() {
    }

    public CartItems(String productId, String productName, double price, List imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
