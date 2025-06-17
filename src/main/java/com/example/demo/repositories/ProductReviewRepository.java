package com.example.demo.repositories;

import com.example.demo.models.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductProductId(Long productId);
    List<ProductReview> findByUserUserId(Long userId);
    List<ProductReview> findByRating(int rating);
    List<ProductReview> findAllByOrderByCreatedAtDesc();

}
