package com.shoply.Products.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "refresh_token")
public class RefreshToken {

    @Id
    private String id;
    private String token;
    private LocalDateTime expiresAt;

    @DBRef
    @Field("user_id")
    private Users user;

    public RefreshToken() {
    }

    public RefreshToken(String token, LocalDateTime expiresAt, Users user) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", expiresAt=" + expiresAt +
                ", user=" + user +
                '}';
    }
}
