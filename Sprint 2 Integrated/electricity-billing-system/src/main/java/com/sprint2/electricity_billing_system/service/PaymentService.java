package com.sprint2.electricity_billing_system.service;

import com.sprint2.electricity_billing_system.dto.PaymentRequest;
import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Payment;
import com.sprint2.electricity_billing_system.entity.PaymentBill;
import com.sprint2.electricity_billing_system.repository.BillRepository;
import com.sprint2.electricity_billing_system.repository.PaymentBillRepository;
import com.sprint2.electricity_billing_system.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentBillRepository paymentBillRepository;
    private final BillRepository billRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentBillRepository paymentBillRepository,
            BillRepository billRepository) {

        this.paymentRepository = paymentRepository;
        this.paymentBillRepository = paymentBillRepository;
        this.billRepository = billRepository;
    }

    // =====================================================
    // US005 - PAY BILL
    // =====================================================

    @Transactional
    public Payment makePayment(PaymentRequest request) {

        // =================================================
        // 1. REQUEST VALIDATION
        // =================================================

        if (request == null) {
            throw new IllegalArgumentException(
                    "Payment details are required");
        }

        // =================================================
        // 2. BILL VALIDATION
        // =================================================

        if (request.getBillId() == null) {
            throw new IllegalArgumentException(
                    "Bill ID is required");
        }

        Bill bill = billRepository
                .findById(request.getBillId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bill not found"));

        // =================================================
        // 3. CARD NUMBER VALIDATION
        // =================================================

        if (request.getCardNumber() == null ||
                request.getCardNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Card number is required");
        }

        if (!request.getCardNumber().matches("\\d{16}")) {

            throw new IllegalArgumentException(
                    "Card number must contain exactly 16 digits");
        }

        // =================================================
        // 4. CARDHOLDER NAME VALIDATION
        // =================================================

        if (request.getCardholderName() == null ||
                request.getCardholderName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Cardholder name is required");
        }

        if (!request.getCardholderName()
                .matches("[a-zA-Z ]+")) {

            throw new IllegalArgumentException(
                    "Cardholder name must contain only letters");
        }

        // =================================================
        // 5. CVV VALIDATION
        // =================================================

        if (request.getCvv() == null ||
                request.getCvv().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "CVV is required");
        }

        /*
         * Accept 3 digits for normal cards
         * or 4 digits for cards such as American Express.
         */
        if (!request.getCvv().matches("\\d{3,4}")) {

            throw new IllegalArgumentException(
                    "CVV must contain 3 or 4 digits");
        }

        // =================================================
        // 6. EXPIRY DATE VALIDATION
        // =================================================

        if (request.getExpiryDate() == null ||
                request.getExpiryDate().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Card expiry date is required");
        }

        validateExpiryDate(request.getExpiryDate());

        // =================================================
        // 7. PAYMENT METHOD VALIDATION
        // =================================================

        if (request.getPaymentMethod() == null ||
                request.getPaymentMethod().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Payment method is required");
        }

        // =================================================
        // 8. PAYMENT AMOUNT VALIDATION
        // =================================================

        if (request.getAmount() == null) {

            throw new IllegalArgumentException(
                    "Payment amount is required");
        }

        if (request.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }

        // =================================================
        // 9. PAYMENT AMOUNT MUST MATCH BILL AMOUNT
        // =================================================

        if (!request.getAmount()
                .equals(bill.getBillAmount())) {

            throw new IllegalArgumentException(
                    "Payment amount does not match bill amount");
        }

        // =================================================
        // 10. CHECK BILL STATUS
        // =================================================

        if ("PAID".equalsIgnoreCase(bill.getStatus())) {

            throw new IllegalArgumentException(
                    "Bill has already been paid");
        }

        // =================================================
        // 11. CREATE PAYMENT
        // =================================================

        Payment payment = new Payment();

        // Transaction ID
        payment.setTransactionId(
                "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        // Receipt Number
        payment.setReceiptNumber(
                "REC-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        // Transaction date
        payment.setPaymentDate(LocalDate.now());

        // Payment mode
        payment.setPaymentMethod(
                request.getPaymentMethod());

        // Credit/Debit
        payment.setTransactionType("DEBIT");

        // Amount
        payment.setTotalAmount(
                request.getAmount());

        // Status
        payment.setPaymentStatus("SUCCESS");

        // =================================================
        // 12. SAVE PAYMENT
        // =================================================

        Payment savedPayment =
                paymentRepository.save(payment);

        // =================================================
        // 13. CREATE PAYMENT-BILL RECORD
        // =================================================

        PaymentBill paymentBill = new PaymentBill();

        paymentBill.setPaymentId(
                savedPayment.getPaymentId());

        paymentBill.setBillId(
                bill.getBillId());

        paymentBill.setAmountPaid(
                request.getAmount());

        paymentBillRepository.save(paymentBill);

        // =================================================
        // 14. UPDATE BILL STATUS
        // =================================================

        bill.setStatus("PAID");

        billRepository.save(bill);

        // =================================================
        // 15. RETURN PAYMENT DETAILS
        // =================================================

        return savedPayment;
    }

    // =====================================================
    // EXPIRY DATE VALIDATION
    // Format: MM/yy
    // =====================================================

    private void validateExpiryDate(String expiryDate) {

        try {

            YearMonth expiry =
                    YearMonth.parse(
                            expiryDate,
                            DateTimeFormatter.ofPattern("MM/yy"));

            YearMonth current =
                    YearMonth.now();

            if (expiry.isBefore(current)) {

                throw new IllegalArgumentException(
                        "Card has expired");
            }

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(
                    "Expiry date must be in MM/yy format");
        }
    }
}