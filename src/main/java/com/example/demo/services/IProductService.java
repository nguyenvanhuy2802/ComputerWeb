package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.dtos.ProductWithRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<ProductDTO> findAll();

    Optional<ProductDTO> findById(Long productId);

    Optional<ProductDTO> findByName(String name);

    List<ProductDTO> searchByName(String keyword);

    List<ProductWithRating> searchProducts(String query, String priceRange, String sortBy);

    List<ProductDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductDTO> findByCategory(Long categoryId);

    ProductDTO save(ProductDTO productDTO);

    ProductDTO update(Long productId, ProductDTO productDTO);

    void deleteById(Long productId);
    Page<ProductDTO> getPaginatedProducts(Pageable pageable);

}
