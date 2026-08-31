package com.sprint2.electricity_billing_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sprint2.electricity_billing_system.entity.Customer;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    Optional<Customer> findByConsumerNumber(
            String consumerNumber
    );

    Optional<Customer> findByUserId(Long userId);

    boolean existsByConsumerNumber(
            String consumerNumber
    );

    boolean existsByEmail(String email);

    boolean existsByUserId(Long userId);
}