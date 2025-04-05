package com.example.demo.repositories;

import com.example.demo.models.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
    List<ProductComment> findByProductProductId(Long productId);
    List<ProductComment> findByUserUserId(Long userId);
}