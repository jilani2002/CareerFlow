package com.careerflow.service;

import com.careerflow.dto.ApplicationCreateRequest;
import com.careerflow.dto.ApplicationResponse;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse applyForJob(ApplicationCreateRequest request);

    List<ApplicationResponse> getMyApplications();

    ApplicationResponse getMyApplication(Long applicationId);

    ApplicationResponse withdrawApplication(Long applicationId);
}