package com.example.Personnel.Management.Repository;

import com.example.Personnel.Management.Entity.EmailOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailOTPRepository extends JpaRepository<EmailOTP, Long> {
    Optional<EmailOTP> findByEmail(String email);
}
