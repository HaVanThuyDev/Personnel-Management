package com.example.Personnel.Management.Service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
