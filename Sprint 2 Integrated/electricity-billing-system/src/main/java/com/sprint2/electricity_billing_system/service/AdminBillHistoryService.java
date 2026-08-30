package com.sprint2.electricity_billing_system.service;

import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.repository.BillRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;
import com.sprint2.electricity_billing_system.repository.PaymentBillRepository;
import com.sprint2.electricity_billing_system.repository.PaymentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class AdminBillHistoryService {

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    public AdminBillHistoryService(
            BillRepository billRepository,
            CustomerRepository customerRepository) {

        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
    }

    public List<Bill> getByCustomerId(Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"));

        List<Bill> bills =
                billRepository.findByCustomerId(customerId);

        sortLatestFirst(bills);

        checkBills(bills);

        return bills;
    }

    public List<Bill> getByConsumerNumber(
            String consumerNumber) {

        if (consumerNumber == null ||
                consumerNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Consumer number is required");
        }

        Customer customer =
                customerRepository
                        .findByConsumerNumber(
                                consumerNumber.trim())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Consumer number not found"));

        List<Bill> bills =
                billRepository.findByCustomerId(
                        customer.getCustomerId());

        sortLatestFirst(bills);

        checkBills(bills);

        return bills;
    }

    public List<Bill> search(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate,
            String status) {

        customerRepository.findById(customerId)
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
                billRepository
                        .findByCustomerIdAndBillDateBetween(
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

        sortLatestFirst(bills);

        checkBills(bills);

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

    private void sortLatestFirst(List<Bill> bills) {

        bills.sort(
                Comparator.comparing(
                        Bill::getBillDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder())));
    }

    private void checkBills(List<Bill> bills) {

        if (bills.isEmpty()) {
            throw new IllegalArgumentException(
                    "No bill history found");
        }
    }
}