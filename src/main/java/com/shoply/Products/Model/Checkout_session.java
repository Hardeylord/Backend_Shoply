package com.shoply.Products.Model;


import com.shoply.Products.rolePermission.CHECKOUT_STATUS;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Checkout_session")
public class Checkout_session {

    @Id
    private String id;
    @DBRef
    private Users userId;
    private List<CartItems> items;
    private double sub_total;
    private String currency;

    @Indexed
    private Instant expiresAt;
    private CHECKOUT_STATUS status;

    public Checkout_session() {

    }
    public Checkout_session(Users userId, List<CartItems> items, double sub_total, String currency, Instant expiresAt, CHECKOUT_STATUS status) {
        this.userId = userId;
        this.items = items;
        this.sub_total = sub_total;
        this.currency = currency;
        this.expiresAt = expiresAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public CHECKOUT_STATUS getCheckoutStatus() {
        return status;
    }

    public void setCheckoutStatus(CHECKOUT_STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Checkout_session{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", items=" + items +
                ", sub_total=" + sub_total +
                ", currency='" + currency + '\'' +
                ", expiresAt=" + expiresAt +
                ", status=" + status +
                '}';
    }
}
