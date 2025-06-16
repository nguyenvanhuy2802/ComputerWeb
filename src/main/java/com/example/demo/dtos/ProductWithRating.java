package com.example.demo.dtos;


import lombok.Data;

@Data
public class ProductWithRating {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Long categoryId;
    private String productImage;
    private double averageRating;
}