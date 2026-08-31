package com.sprint2.electricity_billing_system.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint2.electricity_billing_system.dto.CustomerResponse;
import com.sprint2.electricity_billing_system.dto.RegistrationRequest;
import com.sprint2.electricity_billing_system.dto.RegistrationResponse;
import com.sprint2.electricity_billing_system.entity.AppUser;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.exception.DuplicateResourceException;
import com.sprint2.electricity_billing_system.exception.InvalidRequestException;
import com.sprint2.electricity_billing_system.exception.ResourceNotFoundException;
import com.sprint2.electricity_billing_system.repository.AppUserRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;

@Service
public class CustomerService {

    private static final String CUSTOMER_ROLE = "CUSTOMER";
    private static final String ACTIVE_STATUS = "ACTIVE";

    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            AppUserRepository appUserRepository) {

        this.customerRepository = customerRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public RegistrationResponse registerCustomer(
            RegistrationRequest request) {

        String consumerNumber = request.getConsumerNumber().trim();
        String username = request.getUserId().trim();
        String email = request.getEmail().trim();

        validatePasswordConfirmation(request);

        if (customerRepository.existsByConsumerNumber(consumerNumber)) {
            throw new DuplicateResourceException(
                    "Consumer number is already registered"
            );
        }

        if (appUserRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(
                    "User ID is already registered"
            );
        }

        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Email address is already registered"
            );
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(hashPassword(request.getPassword()));
        appUser.setRole(CUSTOMER_ROLE);

        AppUser savedAppUser = appUserRepository.save(appUser);

        Customer customer = new Customer();
        customer.setConsumerNumber(consumerNumber);
        customer.setUserId(savedAppUser.getId());
        customer.setFullName(request.getCustomerName().trim());
        customer.setAddress(request.getAddress().trim());
        customer.setEmail(email);
        customer.setMobileNumber(request.getMobileNumber().trim());
        customer.setCustomerType(request.getCustomerType().trim());
        customer.setElectricalSection(
                request.getElectricalSection().trim()
        );
        customer.setConnectionStatus(ACTIVE_STATUS);

        Customer savedCustomer = customerRepository.save(customer);

        return new RegistrationResponse(
                "Customer registered successfully",
                savedCustomer.getCustomerId(),
                savedCustomer.getConsumerNumber(),
                savedAppUser.getId(),
                savedAppUser.getUsername()
        );
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByConsumerNumber(
            String consumerNumber) {

        Customer customer = findCustomerByConsumerNumber(consumerNumber);

        return convertToCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    public Customer findCustomerByConsumerNumber(
            String consumerNumber) {

        if (consumerNumber == null || consumerNumber.isBlank()) {
            throw new InvalidRequestException(
                    "Consumer number is required"
            );
        }

        return customerRepository
                .findByConsumerNumber(consumerNumber.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for consumer number: "
                                + consumerNumber
                ));
    }

    private void validatePasswordConfirmation(
            RegistrationRequest request) {

        if (!request.getPassword().equals(
                request.getConfirmPassword())) {

            throw new InvalidRequestException(
                    "Password and confirm password do not match"
            );
        }
    }

    private CustomerResponse convertToCustomerResponse(
            Customer customer) {

        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getConsumerNumber(),
                customer.getUserId(),
                customer.getFullName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getMobileNumber(),
                customer.getCustomerType(),
                customer.getElectricalSection(),
                customer.getConnectionStatus()
        );
    }

    private String hashPassword(String password) {

        try {
            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = messageDigest.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hashBuilder = new StringBuilder();

            for (byte currentByte : encodedHash) {
                hashBuilder.append(
                        String.format("%02x", currentByte)
                );
            }

            return hashBuilder.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "Password hashing algorithm is unavailable",
                    exception
            );
        }
    }
}