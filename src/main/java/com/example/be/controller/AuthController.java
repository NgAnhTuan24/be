package com.example.be.controller;

import com.example.be.dto.RegisterDTO;
import com.example.be.dto.UserDTO;
import com.example.be.entity.User;
import com.example.be.mapper.UserMapper;
import com.example.be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            throw new RuntimeException("Email và mật khẩu không được để trống");
        }

        return userService.login(email, password);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO dto) {
        User user = UserMapper.toRegister(dto);
        User created = userService.createUser(user);
        return ResponseEntity.ok(UserMapper.toDTO(created));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("Thiếu userId");
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        user.setStatus(User.Status.Inactive);
        userService.saveUser(user);

        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
