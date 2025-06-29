package com.example.Personnel.Management.DTO;

import com.example.Personnel.Management.Entity.Role;
import com.example.Personnel.Management.Entity.AccountStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String username;
    private String password;
    private Role role;
    private AccountStatus accountStatus;
}