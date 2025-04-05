package com.example.demo.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartDTO {
    private Long cartId;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDTO> cartItems;
}