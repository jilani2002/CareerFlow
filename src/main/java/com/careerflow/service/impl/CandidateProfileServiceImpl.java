package com.careerflow.service.impl;

import com.careerflow.dto.CandidateProfileResponse;
import com.careerflow.dto.CandidateProfileUpdateRequest;
import com.careerflow.entity.UserEntity;
import com.careerflow.exception.UserNotFoundException;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.CandidateProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final UserRepository userRepository;

    public CandidateProfileServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public CandidateProfileResponse getProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfileResponse response = new CandidateProfileResponse();

        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setAddress(user.getAddress());
        response.setRole(user.getRole());
        return response;
    }

    @Override
    public CandidateProfileResponse updateProfile(CandidateProfileUpdateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();;

        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setMobileNumber(request.getMobileNumber());
        entity.setAddress(request.getAddress());

        UserEntity updatedUser = userRepository.save(entity);

        CandidateProfileResponse response = new CandidateProfileResponse();
        response.setUserId(updatedUser.getUserId());
        response.setFirstName(updatedUser.getFirstName());
        response.setLastName(updatedUser.getLastName());
        response.setEmail(updatedUser.getEmail());
        response.setMobileNumber(updatedUser.getMobileNumber());
        response.setAddress(updatedUser.getAddress());
        response.setRole(updatedUser.getRole());
        return response;
    }
}
