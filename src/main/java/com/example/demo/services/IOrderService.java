package com.example.demo.services;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.enums.OrderStatus;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderDTO> getAllOrders();
    Optional<OrderDTO> getOrderById(Long orderId);
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
    void deleteOrder(Long orderId);
}
