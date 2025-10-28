package com.example.be.controller;

import com.example.be.dto.ContactRequestDTO;
import com.example.be.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "http://localhost:3000") // đổi theo domain FE nếu cần
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody ContactRequestDTO request) {
        try {
            contactService.sendContactMail(request);
            return ResponseEntity.ok("Email gửi thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Lỗi gửi Email: " + e.getMessage());
        }
    }
}
