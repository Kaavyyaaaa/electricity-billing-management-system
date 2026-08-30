package com.sprint2.electricity_billing_system.service;

import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.repository.BillRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    public BillService(BillRepository billRepository,
                       CustomerRepository customerRepository) {

        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
    }

    public java.util.List<Bill> getBillsForCustomer(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return billRepository.findByCustomerId(customerId);
    }

    public Bill addBill(Bill bill) {

        // 1. Customer validation
        if (bill.getCustomerId() == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required");
        }

        Customer customer = customerRepository
                .findById(bill.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"));

        // 2. Billing period validation
        if (bill.getBillingPeriod() == null ||
                bill.getBillingPeriod().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Billing period is required");
        }

        // 3. Bill date validation
        if (bill.getBillDate() == null) {
            throw new IllegalArgumentException(
                    "Bill date is required");
        }

        // 4. Due date validation
        if (bill.getDueDate() == null) {
            throw new IllegalArgumentException(
                    "Due date is required");
        }

        // 5. Due date must be after bill date
        if (bill.getDueDate().isBefore(bill.getBillDate())) {

            throw new IllegalArgumentException(
                    "Due date cannot be before bill date");
        }

        // 6. Disconnection date validation
        if (bill.getDisconnectionDate() != null &&
                bill.getDisconnectionDate()
                        .isBefore(bill.getDueDate())) {

            throw new IllegalArgumentException(
                    "Disconnection date cannot be before due date");
        }

        // 7. Bill amount validation
        if (bill.getBillAmount() == null ||
                bill.getBillAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Bill amount must be greater than zero");
        }

        // 8. Late fee validation
        if (bill.getLateFee() != null &&
                bill.getLateFee() < 0) {

            throw new IllegalArgumentException(
                    "Late fee cannot be negative");
        }

        // 9. Units consumed validation
        if (bill.getUnitsConsumed() != null &&
                bill.getUnitsConsumed() < 0) {

            throw new IllegalArgumentException(
                    "Units consumed cannot be negative");
        }

        // 10. Duplicate bill validation
        boolean duplicate =
                billRepository
                        .existsByCustomerIdAndBillingPeriod(
                                bill.getCustomerId(),
                                bill.getBillingPeriod());

        if (duplicate) {

            throw new IllegalArgumentException(
                    "Bill already exists for this customer " +
                    "and billing period");
        }

        // 11. Generate bill number
        if (bill.getBillNumber() == null ||
                bill.getBillNumber().trim().isEmpty()) {

            bill.setBillNumber(
                    "BILL-" + System.currentTimeMillis());
        }

        // 12. Default late fee
        if (bill.getLateFee() == null) {
            bill.setLateFee(0.0);
        }

        // 13. Default status
        if (bill.getStatus() == null ||
                bill.getStatus().trim().isEmpty()) {

            bill.setStatus("UNPAID");
        }

        // 14. Save bill
        return billRepository.save(bill);
    }
}