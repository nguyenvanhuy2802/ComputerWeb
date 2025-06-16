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
    List<UserDTO> getPart(int limit, int offset);
    List<UserDTO> getOrderedPart(int limit, int offset, String orderBy, String orderDir);
    void changePassword(long userId, String oldPassword, String newPassword);
    UserDTO save(UserDTO userDTO);
    UserDTO update(Long userId, UserDTO userDTO);

    UserDTO changeInfor(Long userId, UserDTO userDTO);

    void deleteById(Long userId);
}
