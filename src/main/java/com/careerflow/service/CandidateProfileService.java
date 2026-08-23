package com.careerflow.service;

import com.careerflow.dto.CandidateProfileResponse;
import com.careerflow.dto.CandidateProfileUpdateRequest;

public interface CandidateProfileService {

    CandidateProfileResponse getProfile();
    CandidateProfileResponse updateProfile(CandidateProfileUpdateRequest request);
}
