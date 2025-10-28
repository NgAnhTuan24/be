    package com.example.be.controller;

    import com.example.be.dto.ChangePasswordDTO;
    import com.example.be.dto.UpdateUserDTO;
    import com.example.be.dto.UserDTO;
    import com.example.be.dto.UserDetailDTO;
    import com.example.be.entity.User;
    import com.example.be.mapper.UserMapper;
    import com.example.be.service.UserService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.PageRequest;

    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @RestController
    @RequestMapping("/api/user")
    public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping
        public List<UserDTO> getAllUsers() {
            return userService.getAllUsers().stream()
                    .filter(u -> u.getRole() == User.Role.User)
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());
        }

        @GetMapping("/{id}")
        public UserDetailDTO getUser(@PathVariable long id) {
            User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            return UserMapper.toDetailDTO(user);
        }

        @PostMapping
        public User createUser(@RequestBody User user) {
            return userService.createUser(user);
        }

        // api dành cho user
        @PutMapping("/{id}")
        public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
            try {
                var updatedUser = userService.updateInfoUser(id, dto);
                return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        // api dành cho user
        @PutMapping("/{id}/password")
        public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO dto) {
            try {
                userService.changePassword(id, dto);
                return ResponseEntity.ok("Đổi mật khẩu thành công!");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @GetMapping("/paged")
        public ResponseEntity<?> getAllUsersPaged(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(required = false) String search
        ) {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userService.searchUsers(search, pageable);

            List<UserDTO> users = userPage.getContent().stream()
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "users", users,
                    "currentPage", userPage.getNumber(),
                    "totalPages", userPage.getTotalPages(),
                    "totalElements", userPage.getTotalElements()
            ));
        }
    }
