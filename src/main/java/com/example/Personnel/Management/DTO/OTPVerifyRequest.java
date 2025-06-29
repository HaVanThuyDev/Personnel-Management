package com.example.Personnel.Management.DTO;

import lombok.Data;

@Data
public class OTPVerifyRequest {
    private String email;
    private String otp;
}
