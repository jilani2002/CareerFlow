package com.careerflow.service;

import com.careerflow.dto.RecruiterApplicationResponse;

import java.util.List;

public interface RecruiterApplicationService {

    List<RecruiterApplicationResponse> getApplicationsForJob(Long jobId);

    RecruiterApplicationResponse getApplicationById(Long applicationId);

    RecruiterApplicationResponse shortlistApplication(Long applicationId);

    RecruiterApplicationResponse rejectApplication(Long applicationId);

    RecruiterApplicationResponse moveToInterview(Long applicationId);

    RecruiterApplicationResponse selectApplication(Long applicationId);
}