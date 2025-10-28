package com.example.be.service;

import com.example.be.dto.ChangePasswordDTO;
import com.example.be.dto.UpdateUserDTO;
import com.example.be.entity.User;
import com.example.be.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository repo) {
        this.userRepository = repo;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        // Kiểm tra miền email
        if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            throw new RuntimeException("Chỉ chấp nhận địa chỉ Gmail hợp lệ (ví dụ: ten@gmail.com)");
        }

        // Kiểm tra trùng email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Kiểm tra trùng số điện thoại
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã được sử dụng");
        }
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        user.setStatus(User.Status.Active);
        userRepository.save(user);

        return user;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateInfoUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        user.setUsername(dto.getUsername());
        user.setAddress(dto.getAddress());

        return userRepository.save(user);
    }

    public void changePassword(Long id, ChangePasswordDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(dto.getOldPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword(dto.getNewPassword());
        userRepository.save(user);
    }

    public Page<User> getAllUsersPaged(Pageable pageable) {
        return userRepository.findAllByRole(User.Role.User, pageable);
    }

    public Page<User> searchUsers(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAllByRole(User.Role.User, pageable);
        }
        return userRepository.searchUsers(keyword, pageable);
    }

}
