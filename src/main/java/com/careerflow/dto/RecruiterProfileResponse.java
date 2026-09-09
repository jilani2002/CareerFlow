package com.careerflow.dto;

import com.careerflow.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private String mobileNumber;
    private String address;
    private String companyName;
}