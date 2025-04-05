package com.example.demo.services;

import com.example.demo.dtos.UserDTO;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(Long userId);
    Optional<UserDTO> findByUsername(String username);
    Optional<UserDTO> findByEmail(String email);
    List<UserDTO> searchByName(String name);
    UserDTO save(UserDTO userDTO);
    UserDTO update(Long userId, UserDTO userDTO);
    void deleteById(Long userId);
}
