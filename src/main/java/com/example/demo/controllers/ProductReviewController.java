package com.example.demo.controllers;

import com.example.demo.dtos.ProductReviewDTO;
import com.example.demo.services.IProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ProductReviewController {

    private final IProductReviewService productReviewService;

    @Autowired
    public ProductReviewController(IProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    // Lấy tất cả đánh giá
    @GetMapping
    public ResponseEntity<List<ProductReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(productReviewService.getAllReviews());
    }

    // Lấy đánh giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductReviewDTO> getReviewById(@PathVariable Long id) {
        Optional<ProductReviewDTO> review = productReviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lấy đánh giá theo ID sản phẩm
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productReviewService.getReviewsByProductId(productId));
    }

    // Lấy đánh giá theo ID người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(productReviewService.getReviewsByUserId(userId));
    }

    // Lấy đánh giá theo số sao
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<ProductReviewDTO>> getReviewsByRating(@PathVariable int rating) {
        return ResponseEntity.ok(productReviewService.getReviewsByRating(rating));
    }

    // Thêm đánh giá mới
    @PostMapping
    public ResponseEntity<ProductReviewDTO> createReview(@RequestBody ProductReviewDTO productReviewDTO) {
        ProductReviewDTO createdReview = productReviewService.createReview(productReviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    // Cập nhật đánh giá
    @PutMapping("/{id}")
    public ResponseEntity<ProductReviewDTO> updateReview(@PathVariable Long id, @RequestBody ProductReviewDTO productReviewDTO) {
        ProductReviewDTO updatedReview = productReviewService.updateReview(id, productReviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    // Xóa đánh giá
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        productReviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    // Tính đánh giá trung bình
    @GetMapping("/product/{productId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        double avgRating = productReviewService.getAverageRatingByProductId(productId);
        return ResponseEntity.ok(avgRating);
    }

}
