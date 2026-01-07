package com.shoply.Products.Response;

import java.time.LocalDateTime;

public class MyErrorResponse {

    public LocalDateTime time;
    public String message;
    public String details;

    public MyErrorResponse(LocalDateTime time, String details, String message) {
        this.time = time;
        this.details = details;
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
