package com.example.demo.services;

import com.example.demo.dtos.OrderItemDTO;
import java.util.List;
import java.util.Optional;

public interface IOrderItemService {
    List<OrderItemDTO> getAllOrderItems();
    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);
    List<OrderItemDTO> getOrderItemsByProductId(Long productId);
    Optional<OrderItemDTO> getOrderItemById(Long orderItemId);
    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);
    OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO);
    void deleteOrderItem(Long orderItemId);
}
