package com.example.demo.services.impls;

import com.example.demo.dtos.CartDTO;
import com.example.demo.models.Cart;
import com.example.demo.models.User;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<CartDTO> findByCustomerId(Long customerId) {
        return cartRepository.findByCustomer_UserId(customerId).map(this::convertToDTO);
    }

    @Override
    public CartDTO createCart(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cartRepository.findByCustomer_UserId(customerId).isPresent()) {
            throw new RuntimeException("Cart already exists for this customer");
        }

        Cart cart = new Cart();
        cart.setCustomer(customer);
        Cart savedCart = cartRepository.save(cart);

        return convertToDTO(savedCart);
    }

    @Override
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setCustomerId(cart.getCustomer().getUserId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        return dto;
    }
}
