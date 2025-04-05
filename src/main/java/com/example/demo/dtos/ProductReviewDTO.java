package com.example.demo.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductReviewDTO {
    private Long reviewId;
    private Long productId;
    private Long userId;
    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;
}