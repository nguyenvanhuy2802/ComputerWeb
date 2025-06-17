package com.example.demo.repositories;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderOrderId(Long orderId);
    List<Payment> findByPaymentMethodAndStatus(PaymentMethod method, PaymentStatus status);

}
