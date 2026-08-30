package com.sprint2.electricity_billing_system.controller;

import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(
            CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    // View all customers
    @GetMapping
    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }
}