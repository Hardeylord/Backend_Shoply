package com.shoply.Products.Model;


import com.shoply.Products.rolePermission.CHECKOUT_STATUS;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "Checkout_session")
public class Checkout_session {

    @Id
    private String id;
    @DBRef
    private Users userId;
    private String firstName;
    private String lastName;
    private String phonenumber;
    private String deliveryAddress;
    private List<CartItems> items;
    private double sub_total;
    private String currency;
    private String idempotencyKey;
    @Indexed
    private Instant expiresAt;
    private CHECKOUT_STATUS status;

    public Checkout_session() {

    }

    public Checkout_session(CHECKOUT_STATUS status, Instant expiresAt, String idempotencyKey, String currency, double sub_total, List<CartItems> items, String deliveryAddress, String phonenumber, String lastName, String firstName, Users userId) {
        this.status = status;
        this.expiresAt = expiresAt;
        this.idempotencyKey = idempotencyKey;
        this.currency = currency;
        this.sub_total = sub_total;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.phonenumber = phonenumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userId = userId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getIdempotency_Key() {
        return idempotencyKey;
    }

    public void setIdempotency_Key(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public CHECKOUT_STATUS getStatus() {
        return status;
    }

    public void setStatus(CHECKOUT_STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Checkout_session{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", items=" + items +
                ", sub_total=" + sub_total +
                ", currency='" + currency + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", expiresAt=" + expiresAt +
                ", status=" + status +
                '}';
    }
}
