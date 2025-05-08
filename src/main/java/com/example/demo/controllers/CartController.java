package com.example.demo.controllers;

import com.example.demo.dtos.CartDTO;
import com.example.demo.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ICartService cartService;

    @Autowired
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    // Lấy giỏ hàng theo customerId
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartDTO> getCartByCustomerId(@PathVariable Long customerId) {
        Optional<CartDTO> cartDTO = cartService.findByCustomerId(customerId);
        return cartDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo giỏ hàng mới cho customer
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<CartDTO> createCart(@PathVariable Long customerId) {
        CartDTO createdCart = cartService.createCart(customerId);
        return ResponseEntity.ok(createdCart);
    }

    // Xóa giỏ hàng theo cartId
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
