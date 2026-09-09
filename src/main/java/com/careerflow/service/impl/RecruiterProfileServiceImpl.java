package com.careerflow.service.impl;

import com.careerflow.dto.RecruiterProfileResponse;
import com.careerflow.dto.RecruiterProfileUpdateRequest;
import com.careerflow.entity.UserEntity;
import com.careerflow.exception.UserNotFoundException;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.RecruiterProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

    private final UserRepository userRepository;

    public RecruiterProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RecruiterProfileResponse getProfile() {
        UserEntity user = getAuthenticatedRecruiter();
        return mapToResponse(user);
    }

    @Override
    public RecruiterProfileResponse updateProfile(RecruiterProfileUpdateRequest request) {

        UserEntity user = getAuthenticatedRecruiter();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setAddress(request.getAddress());
        user.setCompanyName(request.getCompanyName());

        UserEntity updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    private UserEntity getAuthenticatedRecruiter() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    private RecruiterProfileResponse mapToResponse(UserEntity user) {

        RecruiterProfileResponse response = new RecruiterProfileResponse();

        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setMobileNumber(user.getMobileNumber());
        response.setAddress(user.getAddress());
        response.setCompanyName(user.getCompanyName());
        return response;
    }
}