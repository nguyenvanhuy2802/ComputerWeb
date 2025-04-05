package com.example.demo.dtos;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String description;
    private String categoryImage;
}