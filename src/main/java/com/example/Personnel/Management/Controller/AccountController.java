package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.UserDTO;
import com.example.Personnel.Management.Entity.User;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("Người dùng không tồn tại");
        }
        User user = optionalUser.get();
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getAccountStatus() != null) {
            user.setAccountStatus(dto.getAccountStatus());
        }

        userRepository.save(user);
        return ResponseEntity.ok("Cập nhật người dùng thành công");
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Không tìm thấy người dùng để xoá");
        }
        employeeRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        return ResponseEntity.ok("Đã xoá người dùng thành công");
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        List<User> matchedUsers = userRepository.findAll().stream()
                .filter(user ->
                        user.getEmail().toLowerCase().contains(lowerKeyword) ||
                                user.getUsername().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchedUsers);
    }
}
