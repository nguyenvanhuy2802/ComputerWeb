package com.example.demo.services;

import com.example.demo.dtos.CartDTO;
import java.util.Optional;

public interface ICartService {
    Optional<CartDTO> findByCustomerId(Long customerId);
    CartDTO createCart(Long customerId);
    void deleteCart(Long cartId);
}
