package com.example.demo.controllers;

import com.example.demo.dtos.AuthRequest;
import com.example.demo.dtos.AuthResponse;
import com.example.demo.dtos.UserDTO;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.UserDetailsServiceImpl;
import com.example.demo.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(true, "Đăng nhập thành công", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AuthResponse(false, "Sai tài khoản hoặc mật khẩu", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse(false, "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            if (userService.findByUsername(userDTO.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse(false, "Tên đăng nhập đã tồn tại", null));
            }

            if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse(false, "Email đã tồn tại", null));
            }

            UserDTO savedUser = userService.save(userDTO);  
            String token = jwtUtil.generateToken(savedUser.getUsername());

            return ResponseEntity.ok(new AuthResponse(true, "Đăng ký thành công", token));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponse(false, "Đăng ký thất bại: " + e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Client sẽ xoá token sau khi gọi API này
        return ResponseEntity.ok("Đăng xuất thành công");
    }

//localStorage.removeItem("token");
//router.navigate(['/login']);


}
