package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<ProductDTO> findAll();
    Optional<ProductDTO> findById(Long productId);
    Optional<ProductDTO> findByName(String name);
    List<ProductDTO> searchByName(String keyword);
    List<ProductDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<ProductDTO> findByCategory(Long categoryId);
    ProductDTO save(ProductDTO productDTO);
    ProductDTO update(Long productId, ProductDTO productDTO);
    void deleteById(Long productId);
}
