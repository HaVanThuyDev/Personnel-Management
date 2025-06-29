package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.*;
import com.example.Personnel.Management.Entity.*;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Repository.UserRepository;
import com.example.Personnel.Management.Security.JwtUtil;
import com.example.Personnel.Management.Service.EmailService;
import com.example.Personnel.Management.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, UserRegisterDTO> pendingUserStore = new HashMap<>();

    @Override
    public String sendOtpForRegistration(UserRegisterDTO dto) {
        logger.info("Sending OTP for email: {}", dto.getEmail());
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            logger.error("Email đã được sử dụng: {}", dto.getEmail());
            throw new RuntimeException("Email đã được sử dụng!");
        }
        String otp = generateOtp();
        otpStore.put(dto.getEmail(), otp);
        pendingUserStore.put(dto.getEmail(), dto);
        String content = String.format("""
            Xin chào %s,

            Mã OTP của bạn là: %s

            Vui lòng không chia sẻ mã này với bất kỳ ai.
            """, dto.getUsername(), otp);
        emailService.sendEmail(dto.getEmail(), "Mã OTP xác thực đăng ký tài khoản", content);
        logger.info("OTP sent to: {}", dto.getEmail());
        return "Mã OTP đã được gửi đến email!";
    }

    @Override
    @Transactional
    public String verifyOtpAndRegister(OTPVerifyRequest request) {
        logger.info("Verifying OTP for email: {}", request.getEmail());
        String correctOtp = otpStore.get(request.getEmail());

        if (correctOtp == null || !correctOtp.equals(request.getOtp())) {
            logger.error("Invalid or expired OTP for email: {}", request.getEmail());
            throw new RuntimeException("OTP không đúng hoặc đã hết hạn!");
        }

        UserRegisterDTO dto = pendingUserStore.get(request.getEmail());

        Role role;
        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role: {}", dto.getRole());
            throw new RuntimeException("Role không hợp lệ! Chỉ chấp nhận ADMIN, HR, MANAGER, EMPLOYEE, ACCOUNTANT.");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setAccountStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
        otpStore.remove(dto.getEmail());
        pendingUserStore.remove(dto.getEmail());
        logger.info("User registered successfully: {}", dto.getEmail());
        return "Đăng ký tài khoản thành công! Vui lòng chờ được kích hoạt.";
    }

    @Override
    public String loginUser(String email, String password) {
        logger.info("Logging in user with email: {}", email);
        User user = validateUser(email, password);
        String token = jwtUtil.generateToken(user.getEmail());
        logger.info("Login successful for email: {}", email);
        return "Đăng nhập thành công! Token: " + token;
    }

    @Override
    public boolean validateUserCredentials(String email, String password) {
        try {
            validateUser(email, password);
            return true;
        } catch (Exception e) {
            logger.error("Validation failed for email: {}", email, e);
            return false;
        }
    }

    @Override
    public User validateUser(String email, String password) {
        logger.info("Validating user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new RuntimeException("Tài khoản không tồn tại!");
                });

        if (!user.isVerified()) {
            logger.error("User not verified: {}", email);
            throw new RuntimeException("Tài khoản chưa được xác thực! Vui lòng kiểm tra email.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("Incorrect password for email: {}", email);
            throw new RuntimeException("Sai email hoặc mật khẩu!");
        }

        return user;
    }

    private String generateOtp() {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        logger.info("Generated OTP: {}", otp);
        return otp;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(
                        user.getEmail(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole(),
                        user.getAccountStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        logger.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("Không tìm thấy người dùng với ID: " + id);
                });

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        userRepository.save(user);
        logger.info("User updated successfully: {}", id);
        return new UserDTO(
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getAccountStatus()
        );
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("User not found with ID: {}", id);
            throw new EntityNotFoundException("Người dùng không tồn tại với ID: " + id);
        }

        employeeRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        logger.info("User deleted successfully: {}", id);
    }

    @Override
    public String getRoleByEmail(String email) {
        logger.info("Fetching role for email: {}", email);
        return userRepository.findByEmail(email)
                .map(user -> user.getRole().name())
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new RuntimeException("Không tìm thấy người dùng!");
                });
    }

}