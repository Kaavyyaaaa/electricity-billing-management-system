package com.sprint2.electricity_billing_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sprint2.electricity_billing_system.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByCustomerIdOrderByBillDateDesc(Long customerId);

    List<Bill> findByBillIdIn(List<Long> billIds);
}