package com.careerflow.service.impl;

import com.careerflow.dto.RegisterUserRequest;
import com.careerflow.dto.UserResponse;
import com.careerflow.entity.UserEntity;
import com.careerflow.entity.UserRole;
import com.careerflow.exception.InvalidUserRoleException;
import com.careerflow.exception.UserAlreadyExistingException;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistingException("Email already exists in CareerFlow");
        }
        if (UserRole.ADMIN.equals(request.getRole())) {
            throw new InvalidUserRoleException("ADMIN registration is not allowed");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity entity = new UserEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPassword(hashedPassword);
        entity.setRole(request.getRole());
        UserEntity savedEntity = repository.save(entity);
        return getResponse(savedEntity);
    }

    private UserResponse getResponse(UserEntity entity) {
        UserResponse response = new UserResponse();
        response.setUserId(entity.getUserId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setEmail(entity.getEmail());
        response.setRole(entity.getRole());
        response.setMobileNumber(entity.getMobileNumber());
        response.setAddress(entity.getAddress());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}