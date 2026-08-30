package com.sprint2.electricity_billing_system;

import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.repository.BillRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;

    public DataInitializer(
            CustomerRepository customerRepository,
            BillRepository billRepository) {

        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
    }

    @Override
    public void run(String... args) {

        // ==========================================
        // CREATE TEST CUSTOMER
        // ==========================================

        if (customerRepository.count() == 0) {

            Customer customer = new Customer();

            customer.setConsumerNumber("1234567890123");
            customer.setUserId(1001L);
            customer.setFullName("Test Customer");
            customer.setAddress("Coimbatore");
            customer.setEmail("testcustomer@gmail.com");
            customer.setMobileNumber("9876543210");
            customer.setCustomerType("DOMESTIC");
            customer.setElectricalSection("COIMBATORE");
            customer.setConnectionStatus("ACTIVE");

            Customer savedCustomer =
                    customerRepository.save(customer);

            Long customerId =
                    savedCustomer.getCustomerId();

            // ==========================================
            // CREATE TEST BILL 1 - UNPAID
            // ==========================================

            Bill bill1 = new Bill();

            bill1.setCustomerId(customerId);
            bill1.setBillNumber("BILL-1001");
            bill1.setBillingPeriod("August 2026");
            bill1.setBillDate(
                    LocalDate.of(2026, 8, 1));
            bill1.setDueDate(
                    LocalDate.of(2026, 8, 31));
            bill1.setDisconnectionDate(
                    LocalDate.of(2026, 9, 5));
            bill1.setBillAmount(850.00);
            bill1.setLateFee(0.00);
            bill1.setUnitsConsumed(125.0);
            bill1.setStatus("UNPAID");

            billRepository.save(bill1);

            // ==========================================
            // CREATE TEST BILL 2 - PAID
            // ==========================================

            Bill bill2 = new Bill();

            bill2.setCustomerId(customerId);
            bill2.setBillNumber("BILL-1000");
            bill2.setBillingPeriod("July 2026");
            bill2.setBillDate(
                    LocalDate.of(2026, 7, 1));
            bill2.setDueDate(
                    LocalDate.of(2026, 7, 31));
            bill2.setDisconnectionDate(
                    LocalDate.of(2026, 8, 5));
            bill2.setBillAmount(720.00);
            bill2.setLateFee(0.00);
            bill2.setUnitsConsumed(110.0);
            bill2.setStatus("PAID");

            billRepository.save(bill2);

            System.out.println(
                    "====================================");
            System.out.println(
                    "TEST DATA CREATED SUCCESSFULLY");
            System.out.println(
                    "Customer ID: " + customerId);
            System.out.println(
                    "Consumer Number: 1234567890123");
            System.out.println(
                    "Unpaid Bill: BILL-1001");
            System.out.println(
                    "Paid Bill: BILL-1000");
            System.out.println(
                    "====================================");
        }
    }
}