package com.example.demo.services;

import com.example.demo.dtos.ProductReviewDTO;
import java.util.List;
import java.util.Optional;

public interface IProductReviewService {
    List<ProductReviewDTO> getAllReviews();
    List<ProductReviewDTO> getReviewsByProductId(Long productId);
    List<ProductReviewDTO> getReviewsByUserId(Long userId);
    List<ProductReviewDTO> getReviewsByRating(int rating);
    Optional<ProductReviewDTO> getReviewById(Long reviewId);
    ProductReviewDTO createReview(ProductReviewDTO productReviewDTO);
    ProductReviewDTO updateReview(Long reviewId, ProductReviewDTO productReviewDTO);
    void deleteReview(Long reviewId);
    double getAverageRatingByProductId(Long productId);

}
