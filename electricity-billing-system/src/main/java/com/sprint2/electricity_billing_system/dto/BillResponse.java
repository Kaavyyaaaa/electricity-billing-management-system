package com.sprint2.electricity_billing_system.dto;

import java.time.LocalDate;

public class BillResponse {

    private Long billId;
    private String billNumber;
    private LocalDate billDate;
    private String billingPeriod;
    private LocalDate dueDate;
    private Double billAmount;
    private Double unitsConsumed;
    private String status;

    public BillResponse() {
    }

    public BillResponse(
            Long billId,
            String billNumber,
            LocalDate billDate,
            String billingPeriod,
            LocalDate dueDate,
            Double billAmount,
            Double unitsConsumed,
            String status) {

        this.billId = billId;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.billingPeriod = billingPeriod;
        this.dueDate = dueDate;
        this.billAmount = billAmount;
        this.unitsConsumed = unitsConsumed;
        this.status = status;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(Double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}