package com.example.demo.services;

import com.example.demo.dtos.CartItemDTO;
import java.util.List;
import java.util.Optional;

public interface ICartItemService {
    List<CartItemDTO> getCartItemsByCartId(Long cartId);
    Optional<CartItemDTO> getCartItemByCartIdAndProductId(Long cartId, Long productId);
    CartItemDTO addCartItem(Long cartId, Long productId, int quantity);
    CartItemDTO updateCartItem(Long cartItemId, int quantity);
    void removeCartItem(Long cartItemId);
}
