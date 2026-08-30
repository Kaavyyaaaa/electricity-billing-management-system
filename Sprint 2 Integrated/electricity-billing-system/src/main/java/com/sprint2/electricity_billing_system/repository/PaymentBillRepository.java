package com.sprint2.electricity_billing_system.repository;

import com.sprint2.electricity_billing_system.entity.PaymentBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentBillRepository
        extends JpaRepository<PaymentBill, Long> {

    Optional<PaymentBill> findByBillId(Long billId);

    Optional<PaymentBill> findByPaymentId(Long paymentId);
}