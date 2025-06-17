package com.example.demo.repositories;

import com.example.demo.models.Order;
import com.example.demo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCustomerUserId(Long customerId);
    List<Order> findAllByOrderByOrderDateDesc();

}
