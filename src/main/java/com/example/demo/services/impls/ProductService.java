package com.example.demo.services.impls;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.dtos.ProductWithRating;
import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductReviewService productReviewService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductReviewService productReviewService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productReviewService = productReviewService;
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> findById(Long productId) {
        return productRepository.findById(productId).map(this::convertToDTO);
    }

    @Override
    public Optional<ProductDTO> findByName(String name) {
        return productRepository.findByName(name).map(this::convertToDTO);
    }

    @Override
    public List<ProductDTO> searchByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductWithRating> searchProducts(String query, String priceRange, String sortBy) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);

        // ✅ Lọc theo khoảng giá
        if (priceRange != null && priceRange.contains("-")) {
            String[] parts = priceRange.split("-");
            try {
                double min = Double.parseDouble(parts[0]);
                double max = Double.parseDouble(parts[1]);
                products = products.stream()
                        .filter(p -> p.getPrice().doubleValue() >= min && p.getPrice().doubleValue() <= max)
                        .toList();
            } catch (NumberFormatException e) {
                // log warning nếu cần
            }
        }

        // ✅ Chuyển sang DTO (kèm rating)
        List<ProductWithRating> dtos = products.stream()
                .map(product -> {
                    double rating = productReviewService.getAverageRatingByProductId(product.getProductId());
                    ProductWithRating dto = new ProductWithRating();
                    dto.setProductId(product.getProductId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice().doubleValue()); // chuyển từ BigDecimal
                    dto.setStockQuantity(product.getStockQuantity());
                    dto.setCategoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null);
                    dto.setProductImage(product.getProductImage());
                    dto.setAverageRating(rating);
                    return dto;
                })
                .collect(Collectors.toList());

        // ✅ Sắp xếp nếu có
        if (sortBy != null) {
            switch (sortBy) {
                case "price-asc":
                    dtos.sort(Comparator.comparing(ProductWithRating::getPrice));
                    break;
                case "price-desc":
                    dtos.sort(Comparator.comparing(ProductWithRating::getPrice).reversed());
                    break;
                case "rating":
                    dtos.sort(Comparator.comparing(ProductWithRating::getAverageRating).reversed());
                    break;
            }
        }

        return dtos;
    }

    @Override
    public Page<ProductDTO> getPaginatedProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }


    @Override
    public List<ProductDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findByCategory(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(BigDecimal.valueOf(productDTO.getPrice()));
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setProductImage(productDTO.getProductImage());
        product.setCategory(category);

        return convertToDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO update(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(BigDecimal.valueOf(productDTO.getPrice()));
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setProductImage(productDTO.getProductImage());
        product.setCategory(category);

        return convertToDTO(productRepository.save(product));
    }

    @Override
    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice().doubleValue());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setProductImage(product.getProductImage());
        return dto;
    }
}
