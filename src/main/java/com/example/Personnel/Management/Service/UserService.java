package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.*;
import com.example.Personnel.Management.Entity.User;
import java.util.List;

public interface UserService {
    String sendOtpForRegistration(UserRegisterDTO dto);
    String verifyOtpAndRegister(OTPVerifyRequest request);
    String loginUser(String email, String password);
    boolean validateUserCredentials(String email, String password);
    User validateUser(String email, String password);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO dto);
    void deleteUser(Long id);
    String getRoleByEmail(String email);
}