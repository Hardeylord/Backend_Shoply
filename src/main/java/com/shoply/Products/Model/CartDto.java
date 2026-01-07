package com.shoply.Products.Model;

public class CartDto {

    private String prodId;

    public CartDto(String prodId) {
        this.prodId = prodId;
    }
    public CartDto() {
    }
    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "prodId='" + prodId + '\'' +
                '}';
    }
}
