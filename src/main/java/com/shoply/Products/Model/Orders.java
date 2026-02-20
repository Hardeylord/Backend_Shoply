package com.shoply.Products.Model;


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
    private ORDER_STATUS orderStatus;
    private List<CartItems> items;
    private double price;

    public Orders() {

    }

    public Orders(String id, Users owner, ORDER_STATUS orderStatus, List<CartItems> items, double price) {
        this.id = id;
        this.owner = owner;
        this.orderStatus = orderStatus;
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
                ", orderStatus=" + orderStatus +
                ", items=" + items +
                ", price=" + price +
                '}';
    }
}
