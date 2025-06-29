package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.Entity.User;
import com.example.Personnel.Management.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            User user = userService.validateUser(email, password);

            if (user.getAccountStatus() != com.example.Personnel.Management.Entity.AccountStatus.ACTIVE) {
                return ResponseEntity.status(403).body(Map.of("message", "Tài khoản của bạn chưa được kích hoạt!"));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Đăng nhập thành công!",
                    "username", user.getUsername(),
                    "role", user.getRole().name(),
                    "accountStatus", user.getAccountStatus().name()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}