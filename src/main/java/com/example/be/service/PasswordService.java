package com.example.be.service;

import com.example.be.dto.ForgotPasswordRequest;
import com.example.be.entity.PasswordResetToken;
import com.example.be.entity.User;
import com.example.be.repository.PasswordTokenRepository;
import com.example.be.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private PasswordTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void processForgotPassword(ForgotPasswordRequest request) throws MessagingException {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng với email này!");
        }

        User user = userOpt.get();

        // Tạo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        tokenRepository.save(passwordResetToken);

        // Gửi email
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        sendResetEmail(user.getEmail(), resetLink);
    }

    private void sendResetEmail(String toEmail, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Đặt lại mật khẩu - Cathouse");
        helper.setText(
                "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>" +
                        "<p>Nhấn vào liên kết dưới đây để đặt lại mật khẩu (có hiệu lực trong 5 phút):</p>" +
                        "<a href=\"" + resetLink + "\">Đặt lại mật khẩu</a>",
                true);
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            throw new RuntimeException("Token không hợp lệ!");
        }

        if (passwordResetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn!");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);

        // Xoá token sau khi sử dụng
        tokenRepository.delete(passwordResetToken);
    }
}
