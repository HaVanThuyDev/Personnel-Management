package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.OTPVerifyRequest;
import com.example.Personnel.Management.DTO.UserRegisterDTO;
import com.example.Personnel.Management.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        String result = userService.sendOtpForRegistration(userRegisterDTO);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", result
        ));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OTPVerifyRequest request) {
        try {
            String result = userService.verifyOtpAndRegister(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", result
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
