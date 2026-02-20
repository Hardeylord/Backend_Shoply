package com.shoply.Products.Model;


public class Striperesponse {

    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;

    public Striperesponse() {

    }

    public Striperesponse(String status, String message, String sessionId, String sessionUrl) {
        this.status = status;
        this.message = message;
        this.sessionId = sessionId;
        this.sessionUrl = sessionUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    @Override
    public String toString() {
        return "Striperesponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", sessionUrl='" + sessionUrl + '\'' +
                '}';
    }
}
