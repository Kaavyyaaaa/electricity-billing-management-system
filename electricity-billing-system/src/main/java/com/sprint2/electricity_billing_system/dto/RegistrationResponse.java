package com.sprint2.electricity_billing_system.dto;

public class RegistrationResponse {

    private String message;
    private Long customerId;
    private String consumerNumber;
    private Long userId;
    private String username;

    public RegistrationResponse() {
    }

    public RegistrationResponse(
            String message,
            Long customerId,
            String consumerNumber,
            Long userId,
            String username) {

        this.message = message;
        this.customerId = customerId;
        this.consumerNumber = consumerNumber;
        this.userId = userId;
        this.username = username;
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
}