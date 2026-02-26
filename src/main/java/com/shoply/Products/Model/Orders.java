package com.shoply.Products.Model;


import com.shoply.Products.rolePermission.CHECKOUT_STATUS;
import com.shoply.Products.rolePermission.ORDER_STATUS;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
public class Orders {

    @Id
    private String id;
    @DBRef
    private Users owner;
    private String firstName;
    private String lastName;
    private String phonenumber;
    private String deliveryAddress;
    private String email;
    private ORDER_STATUS orderStatus;
    private CHECKOUT_STATUS paymentStatus;
    private List<CartItems> items;
    private double price;

    public Orders() {

    }

    public Orders(Users owner, String firstName, String lastName, String phonenumber, String deliveryAddress, String email, ORDER_STATUS orderStatus, CHECKOUT_STATUS paymentStatus, List<CartItems> items, double price) {
        this.owner = owner;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phonenumber = phonenumber;
        this.deliveryAddress = deliveryAddress;
        this.email = email;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.items = items;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ORDER_STATUS getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(ORDER_STATUS orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }

    public CHECKOUT_STATUS getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(CHECKOUT_STATUS paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id='" + id + '\'' +
                ", owner=" + owner +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", email='" + email + '\'' +
                ", orderStatus=" + orderStatus +
                ", paymentStatus=" + paymentStatus +
                ", items=" + items +
                ", price=" + price +
                '}';
    }
}
