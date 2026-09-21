package com.careerflow.service;

import com.careerflow.dto.JobResponse;
import com.careerflow.entity.EmploymentType;
import org.springframework.data.domain.Page;

public interface JobDiscoveryService {

    Page<JobResponse> searchJobs(
            String keyword,
            String location,
            EmploymentType employmentType,
            Integer experienceMin,
            Integer experienceMax,
            Long salaryMin,
            Long salaryMax,
            int page,
            int size
    );

    JobResponse getJobById(Long jobId);
}