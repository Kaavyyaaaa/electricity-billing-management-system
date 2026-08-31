package com.sprint2.electricity_billing_system.dto;

public class CustomerResponse {

    private Long customerId;
    private String consumerNumber;
    private Long userId;
    private String fullName;
    private String address;
    private String email;
    private String mobileNumber;
    private String customerType;
    private String electricalSection;
    private String connectionStatus;

    public CustomerResponse() {
    }

    public CustomerResponse(
            Long customerId,
            String consumerNumber,
            Long userId,
            String fullName,
            String address,
            String email,
            String mobileNumber,
            String customerType,
            String electricalSection,
            String connectionStatus) {

        this.customerId = customerId;
        this.consumerNumber = consumerNumber;
        this.userId = userId;
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.customerType = customerType;
        this.electricalSection = electricalSection;
        this.connectionStatus = connectionStatus;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getElectricalSection() {
        return electricalSection;
    }

    public void setElectricalSection(String electricalSection) {
        this.electricalSection = electricalSection;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}