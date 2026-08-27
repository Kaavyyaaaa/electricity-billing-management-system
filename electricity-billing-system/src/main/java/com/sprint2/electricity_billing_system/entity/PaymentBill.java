package com.sprint2.electricity_billing_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PAYMENT_BILL")
public class PaymentBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_BILL_ID")
    private Long paymentBillId;

    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "BILL_ID")
    private Long billId;

    @Column(name = "AMOUNT_PAID")
    private Double amountPaid;

    public PaymentBill() {
    }

    public Long getPaymentBillId() {
        return paymentBillId;
    }

    public void setPaymentBillId(Long paymentBillId) {
        this.paymentBillId = paymentBillId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
}