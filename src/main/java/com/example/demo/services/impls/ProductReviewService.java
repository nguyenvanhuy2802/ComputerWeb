package com.example.demo.services.impls;

import com.example.demo.dtos.ProductReviewDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductReview;
import com.example.demo.models.User;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ProductReviewRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.IProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductReviewService implements IProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductReviewService(ProductReviewRepository productReviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.productReviewRepository = productReviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductReviewDTO> getAllReviews() {
        return productReviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewDTO> getReviewsByProductId(Long productId) {
        return productReviewRepository.findByProductProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewDTO> getReviewsByUserId(Long userId) {
        return productReviewRepository.findByUserUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewDTO> getReviewsByRating(int rating) {
        return productReviewRepository.findByRating(rating).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductReviewDTO> getReviewById(Long reviewId) {
        return productReviewRepository.findById(reviewId)
                .map(this::convertToDTO);
    }

    @Override
    public ProductReviewDTO createReview(ProductReviewDTO productReviewDTO) {
        Product product = productRepository.findById(productReviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(productReviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductReview productReview = ProductReview.builder()
                .product(product)
                .user(user)
                .rating(productReviewDTO.getRating())
                .reviewText(productReviewDTO.getReviewText())
                .build();

        return convertToDTO(productReviewRepository.save(productReview));
    }

    @Override
    public ProductReviewDTO updateReview(Long reviewId, ProductReviewDTO productReviewDTO) {
        ProductReview productReview = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        productReview.setRating(productReviewDTO.getRating());
        productReview.setReviewText(productReviewDTO.getReviewText());

        return convertToDTO(productReviewRepository.save(productReview));
    }

    @Override
    public void deleteReview(Long reviewId) {
        productReviewRepository.deleteById(reviewId);
    }

    @Override
    public double getAverageRatingByProductId(Long productId) {
        List<ProductReview> reviews = productReviewRepository.findByProductProductId(productId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToDouble(ProductReview::getRating).average().orElse(0.0);
    }


    private ProductReviewDTO convertToDTO(ProductReview productReview) {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setReviewId(productReview.getReviewId());
        dto.setProductId(productReview.getProduct().getProductId());
        dto.setUserId(productReview.getUser().getUserId());
        dto.setRating(productReview.getRating());
        dto.setReviewText(productReview.getReviewText());
        dto.setCreatedAt(productReview.getCreatedAt());
        return dto;
    }
}
