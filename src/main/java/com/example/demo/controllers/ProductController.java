package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    // ✅ Lấy tất cả sản phẩm
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    // ✅ Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Không tìm thấy sản phẩm"));
    }

    // ✅ Lấy sản phẩm theo tên
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        return productService.findByName(name)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Không tìm thấy sản phẩm"));
    }

    // ✅ Tìm kiếm theo tên (có chứa keyword)
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchByName(keyword));
    }

    // ✅ Tìm theo khoảng giá
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.findByPriceRange(minPrice, maxPrice));
    }

    // ✅ Tìm theo danh mục
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findByCategory(categoryId));
    }

    // ✅ Tạo mới sản phẩm (chỉ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        if (productService.findByName(productDTO.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Tên sản phẩm đã tồn tại");
        }
        ProductDTO saved = productService.save(productDTO);
        return ResponseEntity.ok(saved);
    }

    // ✅ Cập nhật sản phẩm (chỉ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        var existing = productService.findByName(productDTO.getName());
        if (existing.isPresent() && !existing.get().getProductId().equals(id)) {
            return ResponseEntity.badRequest().body("Tên sản phẩm đã tồn tại");
        }

        try {
            ProductDTO updated = productService.update(id, productDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Không tìm thấy sản phẩm để cập nhật");
        }
    }

    // ✅ Xoá sản phẩm (chỉ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.ok("Xoá sản phẩm thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Không tìm thấy sản phẩm để xoá");
        }
    }
}
