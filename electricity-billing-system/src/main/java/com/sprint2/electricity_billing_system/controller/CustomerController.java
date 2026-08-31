package com.sprint2.electricity_billing_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint2.electricity_billing_system.dto.CustomerResponse;
import com.sprint2.electricity_billing_system.dto.RegistrationRequest;
import com.sprint2.electricity_billing_system.dto.RegistrationResponse;
import com.sprint2.electricity_billing_system.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerCustomer(
            @Valid @RequestBody RegistrationRequest request) {

        RegistrationResponse response =
                customerService.registerCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{consumerNumber}/home")
    public ResponseEntity<CustomerResponse> getCustomerHome(
            @PathVariable String consumerNumber) {

        CustomerResponse response =
                customerService.getCustomerByConsumerNumber(
                        consumerNumber
                );

        return ResponseEntity.ok(response);
    }
}