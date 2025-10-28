package com.example.be.controller;

import com.example.be.dto.ForgotPasswordRequest;
import com.example.be.dto.ResetPasswordRequest;
import com.example.be.service.PasswordService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        passwordService.processForgotPassword(request);
        return "Email đặt lại mật khẩu đã được gửi!";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request.getToken(), request.getNewPassword());
        return "Mật khẩu đã được thay đổi thành công!";
    }

}