package com.sprint2.electricity_billing_system.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BillSummaryRequest {

    @NotBlank(message = "Consumer number is required")
    private String consumerNumber;

    @NotEmpty(message = "At least one bill must be selected")
    private List<@NotNull(message = "Bill ID must not be null") Long> billIds;

    public BillSummaryRequest() {
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public List<Long> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }
}