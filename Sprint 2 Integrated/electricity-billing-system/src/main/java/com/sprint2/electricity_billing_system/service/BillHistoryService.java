package com.sprint2.electricity_billing_system.service;

import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.repository.BillRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;
import com.sprint2.electricity_billing_system.repository.PaymentBillRepository;
import com.sprint2.electricity_billing_system.repository.PaymentRepository;
import com.sprint2.electricity_billing_system.entity.Payment;
import com.sprint2.electricity_billing_system.entity.PaymentBill;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class BillHistoryService {

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;
    private final PaymentBillRepository paymentBillRepository;
    private final PaymentRepository paymentRepository;

    public BillHistoryService(
            BillRepository billRepository,
            CustomerRepository customerRepository,
            PaymentBillRepository paymentBillRepository,
            PaymentRepository paymentRepository) {

        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
        this.paymentBillRepository = paymentBillRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Bill> getBillHistory(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String sortBy) {

        if (customerId == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required");
        }

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"));

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        if (startDate == null) {
            startDate = endDate.minusMonths(6);
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date");
        }

        List<Bill> bills =
                billRepository.findByCustomerIdAndBillDateBetween(
                        customerId,
                        startDate,
                        endDate);

        if (status != null &&
                !status.trim().isEmpty()) {

            String requestedStatus =
                    status.trim();

            bills.removeIf(bill ->
                    bill.getStatus() == null ||
                    !bill.getStatus()
                            .equalsIgnoreCase(requestedStatus));
        }

        if (bills.isEmpty()) {
            throw new IllegalArgumentException(
                    "No bill history found for the selected period");
        }

        enrichPaymentDetails(bills);

        if ("amount".equalsIgnoreCase(sortBy) ||
                "billAmount".equalsIgnoreCase(sortBy)) {

            bills.sort(
                    Comparator.comparing(
                            Bill::getBillAmount,
                            Comparator.nullsLast(
                                    Comparator.naturalOrder())));

        } else if ("dueDate".equalsIgnoreCase(sortBy)) {

            bills.sort(
                    Comparator.comparing(
                            Bill::getDueDate,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder())));

        } else {

            // Default = latest Bill Date first
            bills.sort(
                    Comparator.comparing(
                            Bill::getBillDate,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder())));
        }

        return bills;
    }

    private void enrichPaymentDetails(List<Bill> bills) {
        for (Bill bill : bills) {
            paymentBillRepository.findByBillId(bill.getBillId()).ifPresent(pb ->
                paymentRepository.findById(pb.getPaymentId()).ifPresent(payment -> {
                    bill.setPaymentDate(payment.getPaymentDate());
                    bill.setPaymentMode(payment.getPaymentMethod());
                })
            );
        }
    }
}