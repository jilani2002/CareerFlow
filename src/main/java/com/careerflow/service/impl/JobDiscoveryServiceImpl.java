package com.careerflow.service.impl;

import com.careerflow.dto.JobResponse;
import com.careerflow.entity.EmploymentType;
import com.careerflow.entity.JobEntity;
import com.careerflow.entity.JobStatus;
import com.careerflow.exception.JobNotFoundException;
import com.careerflow.repository.JobRepository;
import com.careerflow.service.JobDiscoveryService;
import com.careerflow.specification.JobSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class JobDiscoveryServiceImpl implements JobDiscoveryService {

    private final JobRepository jobRepository;

    public JobDiscoveryServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Page<JobResponse> searchJobs(
            String keyword,
            String location,
            EmploymentType employmentType,
            Integer experienceMin,
            Integer experienceMax,
            Long salaryMin,
            Long salaryMax,
            int page,
            int size) {

        if (experienceMin != null && experienceMin < 0) {
            throw new IllegalArgumentException("Minimum experience cannot be negative");
        }

        if (experienceMax != null && experienceMax < 0) {
            throw new IllegalArgumentException("Maximum experience cannot be negative");
        }

        if (salaryMin != null && salaryMin < 0) {
            throw new IllegalArgumentException("Minimum salary cannot be negative");
        }

        if (salaryMax != null && salaryMax < 0) {
            throw new IllegalArgumentException("Maximum salary cannot be negative");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        if (size <= 0 || size > 50) {
            throw new IllegalArgumentException("Page size must be between 1 and 50");
        }

        if (experienceMin != null && experienceMax != null && experienceMin > experienceMax) {
            throw new IllegalArgumentException("Minimum experience cannot be greater than maximum experience");
        }

        if (salaryMin != null && salaryMax != null && salaryMin > salaryMax) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

        Page<JobEntity> jobs = jobRepository.findAll(
                JobSpecification.filterJobs(
                        keyword,
                        location,
                        employmentType,
                        experienceMin,
                        experienceMax,
                        salaryMin,
                        salaryMax
                ),
                pageable
        );

        return jobs.map(this::mapToResponse);
    }

    @Override
    public JobResponse getJobById(Long jobId) {

        JobEntity job = jobRepository
                .findById(jobId)
                .filter(jobEntity ->
                        jobEntity.getStatus() == JobStatus.OPEN
                )
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found")
                );

        return mapToResponse(job);
    }

    private JobResponse mapToResponse(JobEntity job) {

        JobResponse response = new JobResponse();

        response.setJobId(job.getJobId());
        response.setRecruiterId(job.getRecruiter().getUserId());
        response.setCompanyName(job.getCompanyName());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setExperienceMin(job.getExperienceMin());
        response.setExperienceMax(job.getExperienceMax());
        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());
        response.setSkills(job.getSkills());
        response.setStatus(job.getStatus());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        return response;
    }
}