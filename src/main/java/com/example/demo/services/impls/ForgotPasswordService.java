package com.example.demo.services.impls;

import com.example.demo.models.PasswordResetToken;
import com.example.demo.models.User;
import com.example.demo.repositories.PasswordResetTokenRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void generateAndSendOtp(String email) {
        if (userRepo.findByEmail(email).isEmpty()) {
            throw new RuntimeException("Email không tồn tại");
        }

        String otp = String.valueOf((int)((Math.random() * 900000) + 100000)); // Random 6 số
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setOtp(otp);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        tokenRepo.deleteByEmail(email); // Xoá OTP cũ nếu có
        tokenRepo.save(token);

        emailService.sendOtpEmail(email, otp);
    }

    @Transactional
    public String verifyOtpAndGenerateTempToken(String email, String otp) {
        PasswordResetToken entity = tokenRepo.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("OTP không hợp lệ hoặc hết hạn"));

        if (entity.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn");
        }

        String tempToken = UUID.randomUUID().toString();
        entity.setTempToken(tempToken); // lưu token tạm
        tokenRepo.save(entity);

        return tempToken; // Trả về FE để sau dùng reset password
    }

    @Transactional
    public void resetPassword(String email, String tempToken, String newPassword) {
        PasswordResetToken token = tokenRepo.findByEmailAndTempToken(email, tempToken)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc hết hạn"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        tokenRepo.deleteByEmail(email); // Xoá token reset sau khi đổi mật khẩu
    }
}
