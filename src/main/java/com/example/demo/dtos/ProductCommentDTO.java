package com.example.demo.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductCommentDTO {
    private Long commentId;
    private Long productId;
    private Long userId;
    private String commentText;
    private LocalDateTime createdAt;
}