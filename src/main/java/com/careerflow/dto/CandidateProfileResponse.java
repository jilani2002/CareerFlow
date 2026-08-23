package com.careerflow.dto;

import com.careerflow.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private String mobileNumber;
    private String address;
}