package com.example.demo.controllers;

import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.services.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final IOrderItemService orderItemService;

    @Autowired
    public OrderItemController(IOrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.getAllOrderItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(orderItemService.getOrderItemsByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        return ResponseEntity.ok(orderItemService.createOrderItem(orderItemDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable Long id,
            @RequestBody OrderItemDTO orderItemDTO
    ) {
        return ResponseEntity.ok(orderItemService.updateOrderItem(id, orderItemDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
