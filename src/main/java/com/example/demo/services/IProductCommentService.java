package com.example.demo.services;

import com.example.demo.dtos.ProductCommentDTO;
import java.util.List;
import java.util.Optional;

public interface IProductCommentService {
    List<ProductCommentDTO> getAllComments();
    List<ProductCommentDTO> getCommentsByProductId(Long productId);
    List<ProductCommentDTO> getCommentsByUserId(Long userId);
    Optional<ProductCommentDTO> getCommentById(Long commentId);
    ProductCommentDTO createComment(ProductCommentDTO productCommentDTO);
    ProductCommentDTO updateComment(Long commentId, ProductCommentDTO productCommentDTO);
    void deleteComment(Long commentId);
}
