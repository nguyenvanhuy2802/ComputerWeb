package com.example.demo.controllers;

import com.example.demo.dtos.CartItemDTO;
import com.example.demo.services.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final ICartItemService cartItemService;

    @Autowired
    public CartItemController(ICartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // 🔍 Lấy tất cả sản phẩm trong giỏ theo cartId
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDTO>> getCartItemsByCartId(@PathVariable Long cartId) {
        List<CartItemDTO> items = cartItemService.getCartItemsByCartId(cartId);
        return ResponseEntity.ok(items);
    }

    // 🔍 Lấy một sản phẩm trong giỏ theo cartId và productId
    @GetMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<CartItemDTO> getCartItem(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ) {
        Optional<CartItemDTO> item = cartItemService.getCartItemByCartIdAndProductId(cartId, productId);
        return item.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ➕ Thêm sản phẩm vào giỏ hàng
    @PostMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<CartItemDTO> addCartItem(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        CartItemDTO addedItem = cartItemService.addCartItem(cartId, productId, quantity);
        return ResponseEntity.ok(addedItem);
    }

    // ✏️ Cập nhật số lượng sản phẩm
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDTO> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ) {
        CartItemDTO updatedItem = cartItemService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    // ❌ Xoá sản phẩm khỏi giỏ
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
