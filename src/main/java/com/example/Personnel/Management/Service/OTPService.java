package com.example.Personnel.Management.Service;

public interface OTPService {
    void generateAndSendOTP(String email);
    boolean validateOTP(String email, String otpCode);
}
