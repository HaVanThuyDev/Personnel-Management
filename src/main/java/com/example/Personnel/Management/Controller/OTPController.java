package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.OTPVerifyRequest;
import com.example.Personnel.Management.Service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController {

    private final OTPService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        otpService.generateAndSendOTP(email);
        return ResponseEntity.ok("OTP đã được gửi tới email: " + email);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerifyRequest request) {
        boolean isValid = otpService.validateOTP(request.getEmail(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok(" Mã OTP hợp lệ. Bạn có thể tiếp tục đăng ký.");
        } else {
            return ResponseEntity.badRequest().body(" Mã OTP không đúng hoặc đã hết hạn.");
        }
    }

}
