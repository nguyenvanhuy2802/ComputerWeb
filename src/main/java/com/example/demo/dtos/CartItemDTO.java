package com.example.demo.dtos;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private Integer quantity;
}