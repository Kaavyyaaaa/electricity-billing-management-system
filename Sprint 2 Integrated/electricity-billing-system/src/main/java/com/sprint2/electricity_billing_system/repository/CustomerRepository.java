package com.sprint2.electricity_billing_system.repository;

import com.sprint2.electricity_billing_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    Optional<Customer> findByConsumerNumber(
            String consumerNumber);
}