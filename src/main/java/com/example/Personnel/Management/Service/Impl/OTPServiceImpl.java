package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.Entity.EmailOTP;
import com.example.Personnel.Management.Repository.EmailOTPRepository;
import com.example.Personnel.Management.Service.EmailService;
import com.example.Personnel.Management.Service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final EmailOTPRepository otpRepository;
    private final EmailService emailService;

    @Override
    public void generateAndSendOTP(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        EmailOTP emailOTP = otpRepository.findByEmail(email).orElse(new EmailOTP());
        emailOTP.setEmail(email);
        emailOTP.setOtpCode(otp);
        emailOTP.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(emailOTP);

        String subject = "Mã OTP xác thực";
        String body = "Mã OTP của bạn là: " + otp + "\nMã này có hiệu lực trong 5 phút.";
        emailService.sendEmail(email, subject, body);
    }

    @Override
    public boolean validateOTP(String email, String otpCode) {
        Optional<EmailOTP> optionalOTP = otpRepository.findByEmail(email);
        return optionalOTP
                .filter(otp -> otp.getOtpCode().equals(otpCode))
                .filter(otp -> otp.getExpiryTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
