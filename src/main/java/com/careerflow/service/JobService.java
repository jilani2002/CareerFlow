package com.careerflow.service;

import com.careerflow.dto.JobCreateRequest;
import com.careerflow.dto.JobResponse;
import com.careerflow.dto.JobUpdateRequest;

import java.util.List;

public interface JobService {

    JobResponse createJob(JobCreateRequest request);

    List<JobResponse> getMyJobs();

    JobResponse getJobById(Long jobId);

    JobResponse updateJob(Long jobId, JobUpdateRequest request);

    void deleteJob(Long jobId);

    JobResponse closeJob(Long jobId);
}