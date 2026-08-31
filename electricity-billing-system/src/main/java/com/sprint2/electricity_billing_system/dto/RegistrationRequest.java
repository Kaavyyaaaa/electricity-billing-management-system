package com.sprint2.electricity_billing_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank(message = "Consumer number is required")
    @Size(
            max = 13,
            message = "Consumer number must not exceed 13 characters"
    )
    private String consumerNumber;

    @NotBlank(message = "Customer name is required")
    @Size(
            min = 3,
            max = 255,
            message = "Customer name must contain between 3 and 255 characters"
    )
    private String customerName;

    @NotBlank(message = "Address is required")
    @Size(
            max = 255,
            message = "Address must not exceed 255 characters"
    )
    private String address;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(
            max = 255,
            message = "Email must not exceed 255 characters"
    )
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits"
    )
    private String mobileNumber;

    @NotBlank(message = "Customer type is required")
    @Size(
            max = 255,
            message = "Customer type must not exceed 255 characters"
    )
    private String customerType;

    @NotBlank(message = "Electrical section is required")
    @Size(
            max = 255,
            message = "Electrical section must not exceed 255 characters"
    )
    private String electricalSection;

    @NotBlank(message = "User ID is required")
    @Size(
            min = 5,
            max = 255,
            message = "User ID must contain between 5 and 255 characters"
    )
    private String userId;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 255,
            message = "Password must contain between 8 and 255 characters"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    public RegistrationRequest() {
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}