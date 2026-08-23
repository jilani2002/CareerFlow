package com.careerflow.dto;

import com.careerflow.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private String message;
}
