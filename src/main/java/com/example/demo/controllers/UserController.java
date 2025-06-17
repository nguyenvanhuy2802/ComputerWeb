package com.example.demo.controllers;

import com.example.demo.dtos.AuthResponse;
import com.example.demo.dtos.ChangePasswordRequest;
import com.example.demo.dtos.UserDTO;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.UserDetailsServiceImpl;
import com.example.demo.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Lấy thông tin người dùng theo ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(currentUser.getId());
    }
    @PostMapping() 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adduser(@RequestBody UserDTO userDTO) {
        try {
            if (userService.findByUsername(userDTO.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse(false, "Tên đăng nhập đã tồn tại", null));
            }

            if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse(false, "Email đã tồn tại", null));
            }

            userService.save(userDTO);
            return ResponseEntity.ok(new AuthResponse(true, "Đăng ký thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponse(false, "Đăng ký thất bại: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {

        try {

            return userService.findById(id).map(user -> {
                if (!userDTO.getUsername().equals(user.getUsername()) &&
                        userService.findByUsername(userDTO.getUsername()).isPresent()) {
                    return ResponseEntity.badRequest()
                            .body(new AuthResponse(false, "Tên đăng nhập đã tồn tại", null));
                }

                if (!userDTO.getEmail().equals(user.getEmail()) &&
                        userService.findByEmail(userDTO.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest()
                            .body(new AuthResponse(false, "Email đã tồn tại", null));
                }

                UserDTO updated = userService.update(id, userDTO);
                UserDetails userDetails = userDetailsService.loadUserByUsername(updated.getUsername());
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(true, "Đổi thông tin thành công", token));
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponse(false, "Đổi thông thất bại: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống");
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchByName(name));
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getPagedUsers(@RequestParam int limit, @RequestParam int offset) {
        return ResponseEntity.ok(userService.getPart(limit, offset));
    }

    @GetMapping("/sorted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getOrderedUsers(
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam String orderBy,
            @RequestParam String orderDir
    ) {
        return ResponseEntity.ok(userService.getOrderedPart(limit, offset, orderBy, orderDir));
    }
    @PutMapping("/{id}/changeInfor")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<?> changeInfor(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            return userService.findById(id).map(user -> {
                if (!userDTO.getEmail().equals(user.getEmail()) &&
                        userService.findByEmail(userDTO.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest()
                            .body(new AuthResponse(false, "Email đã tồn tại", null));
                }

                UserDTO updated = userService.changeInfor(id, userDTO);

                return ResponseEntity.ok(new AuthResponse(true, "Đổi thông tin thành công",null));
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponse(false, "Đổi thông thất bại: " + e.getMessage(), null));
        }
    }
}
