package com.sprint2.electricity_billing_system.repository;

import com.sprint2.electricity_billing_system.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    boolean existsByCustomerIdAndBillingPeriod(
            Long customerId,
            String billingPeriod);

    List<Bill> findByCustomerId(Long customerId);

    List<Bill> findByCustomerIdAndBillDateBetween(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate);

    List<Bill> findByCustomerIdAndStatusIgnoreCase(
            Long customerId,
            String status);

    List<Bill> findByBillingPeriod(String billingPeriod);
}