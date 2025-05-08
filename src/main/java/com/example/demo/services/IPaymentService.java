package com.example.demo.services;

import com.example.demo.dtos.PaymentDTO;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    List<PaymentDTO> getAllPayments();
    Optional<PaymentDTO> getPaymentById(Long paymentId);
    List<PaymentDTO> getPaymentsByOrderId(Long orderId);
    PaymentDTO createPayment(PaymentDTO dto);
    PaymentDTO updatePayment(Long paymentId, PaymentDTO dto);
    void deletePayment(Long paymentId);
}
