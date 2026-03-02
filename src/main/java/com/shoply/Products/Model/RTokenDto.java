package com.shoply.Products.Model;

public class RTokenDto {
    private String rToken;

    public RTokenDto(String rToken) {
        this.rToken = rToken;
    }
    public RTokenDto() {
    }

    public String getrToken() {
        return rToken;
    }

    public void setrToken(String rToken) {
        this.rToken = rToken;
    }

    @Override
    public String toString() {
        return "RTokenDto{" +
                "rToken='" + rToken + '\'' +
                '}';
    }
}
