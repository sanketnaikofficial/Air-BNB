package com.PaymentService.Repository;

import com.PaymentService.Entity.Payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByStripeSessionId(String stripeSessionId);
}

