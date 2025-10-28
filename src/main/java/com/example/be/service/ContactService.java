package com.example.be.service;

import com.example.be.dto.ContactRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactMail(ContactRequestDTO request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("luckytme2004@gmail.com"); // mail admin
        message.setSubject("Liên hệ từ " + request.getName() + " - " + request.getSubject());
        message.setText(
                "Người gửi: " + request.getName() +
                        "\nEmail: " + request.getEmail() +
                        "\nSố điện thoại: " + (request.getPhone() != null ? request.getPhone() : "Không cung cấp") +
                        "\n\nNội dung:\n" + request.getMessage()
        );

        mailSender.send(message);
    }
}
