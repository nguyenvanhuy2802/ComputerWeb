package com.example.demo.services;

import com.example.demo.dtos.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<CategoryDTO> findAll();
    Optional<CategoryDTO> findById(Long categoryId);
    Optional<CategoryDTO> findByName(String name);
    List<CategoryDTO> searchByName(String keyword);
    CategoryDTO save(CategoryDTO categoryDTO);
    CategoryDTO update(Long categoryId, CategoryDTO categoryDTO);
    void deleteById(Long categoryId);
}
