package com.example.Personnel.Management.DTO;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
    private String role;
}
