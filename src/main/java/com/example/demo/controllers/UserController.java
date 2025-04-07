package com.example.demo.controllers;

import com.example.demo.dtos.UserDTO;
import com.example.demo.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
    @GetMapping("/paging")
    public List<UserDTO> getUserPaging(
            @RequestParam int limit,
            @RequestParam int offset
    ) {
        return userService.getPart(limit, offset);
    }

    @GetMapping("/paging/sort")
    public List<UserDTO> getUserPagingWithSort(
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam String orderBy,
            @RequestParam(defaultValue = "asc") String orderDir
    ) {
        return userService.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    @PutMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
            @RequestParam String newPassword
    ) {
        userService.changePassword(id, newPassword);
    }
}
