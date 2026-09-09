package com.careerflow.service;

import com.careerflow.dto.RecruiterProfileResponse;
import com.careerflow.dto.RecruiterProfileUpdateRequest;

public interface RecruiterProfileService {

    RecruiterProfileResponse getProfile();

    RecruiterProfileResponse updateProfile(RecruiterProfileUpdateRequest request);
}