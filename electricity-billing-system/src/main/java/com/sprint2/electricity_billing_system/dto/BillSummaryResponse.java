package com.sprint2.electricity_billing_system.dto;

import java.util.List;

public class BillSummaryResponse {

    private String consumerNumber;
    private Integer selectedBillCount;
    private List<BillResponse> selectedBills;
    private Double totalAmount;

    public BillSummaryResponse() {
    }

    public BillSummaryResponse(
            String consumerNumber,
            Integer selectedBillCount,
            List<BillResponse> selectedBills,
            Double totalAmount) {

        this.consumerNumber = consumerNumber;
        this.selectedBillCount = selectedBillCount;
        this.selectedBills = selectedBills;
        this.totalAmount = totalAmount;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public Integer getSelectedBillCount() {
        return selectedBillCount;
    }

    public void setSelectedBillCount(Integer selectedBillCount) {
        this.selectedBillCount = selectedBillCount;
    }

    public List<BillResponse> getSelectedBills() {
        return selectedBills;
    }

    public void setSelectedBills(List<BillResponse> selectedBills) {
        this.selectedBills = selectedBills;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}