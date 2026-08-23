package com.careerflow.service;

import com.careerflow.dto.LoginRequest;
import com.careerflow.dto.LoginResponse;
import com.careerflow.dto.RegisterUserRequest;
import com.careerflow.dto.UserResponse;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest request);
    LoginResponse login(LoginRequest request);
}
