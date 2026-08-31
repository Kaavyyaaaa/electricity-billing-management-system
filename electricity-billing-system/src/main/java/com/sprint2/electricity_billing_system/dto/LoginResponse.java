package com.sprint2.electricity_billing_system.dto;

public class LoginResponse {

    private String message;
    private Long customerId;
    private String consumerNumber;
    private Long userId;
    private String username;
    private String fullName;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String message,
            Long customerId,
            String consumerNumber,
            Long userId,
            String username,
            String fullName,
            String role) {

        this.message = message;
        this.customerId = customerId;
        this.consumerNumber = consumerNumber;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}