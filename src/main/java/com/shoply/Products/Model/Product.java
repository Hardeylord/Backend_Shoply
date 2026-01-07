package com.shoply.Products.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @JsonProperty("_id")
    private String id;
    private int prodId;
    private String name;
    private String desc;
    private Double price;
    private Double rating;
    private List image;

    public Product() {
    }

    public Product(String id, int prodId, String name, String desc, Double price, Double rating, List image) {
        this.id = id;
        this.prodId = prodId;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.rating = rating;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List getImage() {
        return image;
    }

    public void setImage(List image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", prodId=" + prodId +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", image=" + image +
                '}';
    }
}
