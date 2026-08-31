package com.sprint2.electricity_billing_system.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint2.electricity_billing_system.dto.LoginRequest;
import com.sprint2.electricity_billing_system.dto.LoginResponse;
import com.sprint2.electricity_billing_system.entity.AppUser;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.exception.InvalidCredentialsException;
import com.sprint2.electricity_billing_system.repository.AppUserRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final CustomerRepository customerRepository;

    public AuthService(
            AppUserRepository appUserRepository,
            CustomerRepository customerRepository) {

        this.appUserRepository = appUserRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String username = request.getUserId().trim();
        String enteredPasswordHash =
                hashPassword(request.getPassword());

        AppUser appUser = appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid user ID or password"
                ));

        if (!enteredPasswordHash.equals(appUser.getPassword())) {
            throw new InvalidCredentialsException(
                    "Invalid user ID or password"
            );
        }

        Customer customer = customerRepository
                .findByUserId(appUser.getId())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Customer account is not linked to this user"
                ));

        return new LoginResponse(
                "Login successful",
                customer.getCustomerId(),
                customer.getConsumerNumber(),
                appUser.getId(),
                appUser.getUsername(),
                customer.getFullName(),
                appUser.getRole()
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