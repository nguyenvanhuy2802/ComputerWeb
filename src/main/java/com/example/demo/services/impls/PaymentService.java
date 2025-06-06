package com.example.demo.services.impls;

import com.example.demo.dtos.PaymentDTO;
import com.example.demo.models.Order;
import com.example.demo.models.Payment;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.PaymentRepository;
import com.example.demo.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentDTO> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(this::convertToDTO);
    }

    @Override
    public List<PaymentDTO> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderOrderId(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = Payment.builder()
                .order(order)
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .status(dto.getStatus())
                .build();

        return convertToDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO dto) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(dto.getStatus());
        payment.setPaymentDate(LocalDateTime.now());
        return convertToDTO(paymentRepository.save(payment));
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(payment.getOrder().getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}
