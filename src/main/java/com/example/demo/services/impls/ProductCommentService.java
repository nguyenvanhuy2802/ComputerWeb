package com.example.demo.services.impls;

import com.example.demo.dtos.ProductCommentDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductComment;
import com.example.demo.models.User;
import com.example.demo.repositories.ProductCommentRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.IProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCommentService implements IProductCommentService {

    private final ProductCommentRepository productCommentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductCommentService(ProductCommentRepository productCommentRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.productCommentRepository = productCommentRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductCommentDTO> getAllComments() {
        return productCommentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCommentDTO> getCommentsByProductId(Long productId) {
        return productCommentRepository.findByProductProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCommentDTO> getCommentsByUserId(Long userId) {
        return productCommentRepository.findByUserUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductCommentDTO> getCommentById(Long commentId) {
        return productCommentRepository.findById(commentId)
                .map(this::convertToDTO);
    }

    @Override
    public ProductCommentDTO createComment(ProductCommentDTO productCommentDTO) {
        Product product = productRepository.findById(productCommentDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(productCommentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductComment productComment = ProductComment.builder()
                .product(product)
                .user(user)
                .commentText(productCommentDTO.getCommentText())
                .build();

        return convertToDTO(productCommentRepository.save(productComment));
    }

    @Override
    public ProductCommentDTO updateComment(Long commentId, ProductCommentDTO productCommentDTO) {
        ProductComment productComment = productCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        productComment.setCommentText(productCommentDTO.getCommentText());

        return convertToDTO(productCommentRepository.save(productComment));
    }

    @Override
    public void deleteComment(Long commentId) {
        productCommentRepository.deleteById(commentId);
    }

    private ProductCommentDTO convertToDTO(ProductComment productComment) {
        ProductCommentDTO dto = new ProductCommentDTO();
        dto.setCommentId(productComment.getCommentId());
        dto.setProductId(productComment.getProduct().getProductId());
        dto.setUserId(productComment.getUser().getUserId());
        dto.setCommentText(productComment.getCommentText());
        dto.setCreatedAt(productComment.getCreatedAt());
        return dto;
    }
}
