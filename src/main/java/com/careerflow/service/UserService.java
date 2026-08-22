package com.careerflow.service;

import com.careerflow.dto.RegisterUserRequest;
import com.careerflow.dto.UserResponse;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest request);
}
