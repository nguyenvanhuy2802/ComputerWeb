package com.example.demo.controllers;

import com.example.demo.dtos.CategoryDTO;
import com.example.demo.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
        public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Không tìm thấy danh mục"));
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        return categoryService.findByName(name)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Không tìm thấy danh mục"));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategories(@RequestParam String keyword) {
        return ResponseEntity.ok(categoryService.searchByName(keyword));
    }

    // ✅ Tạo mới danh mục với kiểm tra trùng tên + phân quyền ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryService.findByName(categoryDTO.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Tên danh mục đã tồn tại");
        }
        CategoryDTO saved = categoryService.save(categoryDTO);
        return ResponseEntity.ok(saved);
    }

    // ✅ Cập nhật danh mục với kiểm tra trùng tên + phân quyền ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        var existing = categoryService.findByName(categoryDTO.getName());
        if (existing.isPresent() && !existing.get().getCategoryId().equals(id)) {
            return ResponseEntity.badRequest().body("Tên danh mục đã tồn tại");
        }

        try {
            CategoryDTO updated = categoryService.update(id, categoryDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Không tìm thấy danh mục để cập nhật");
        }
    }

    // ✅ Xoá danh mục (chỉ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return ResponseEntity.ok("Xoá danh mục thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Không tìm thấy danh mục để xoá");
        }
    }
}
