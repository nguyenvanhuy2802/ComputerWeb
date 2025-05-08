package com.example.demo.dtos;

import com.example.demo.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address; 
    private String username;
    private Role role;
    private String profileImage;
    private String password;
}