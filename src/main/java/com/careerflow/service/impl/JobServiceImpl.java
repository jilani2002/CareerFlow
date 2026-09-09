package com.careerflow.service.impl;

import com.careerflow.dto.JobCreateRequest;
import com.careerflow.dto.JobResponse;
import com.careerflow.dto.JobUpdateRequest;
import com.careerflow.entity.JobEntity;
import com.careerflow.entity.JobStatus;
import com.careerflow.entity.UserEntity;
import com.careerflow.exception.JobNotFoundException;
import com.careerflow.exception.UserNotFoundException;
import com.careerflow.repository.JobRepository;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobResponse createJob(JobCreateRequest request) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        validateExperience(request.getExperienceMin(), request.getExperienceMax());

        validateSalary(request.getSalaryMin(), request.getSalaryMax());

        JobEntity job = new JobEntity();

        job.setRecruiter(recruiter);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceMin(request.getExperienceMin());
        job.setExperienceMax(request.getExperienceMax());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setSkills(request.getSkills());
        job.setStatus(JobStatus.OPEN);

        JobEntity savedJob = jobRepository.save(job);

        return mapToResponse(savedJob);
    }

    @Override
    public List<JobResponse> getMyJobs() {

        UserEntity recruiter = getAuthenticatedRecruiter();

        return jobRepository.findByRecruiter(recruiter)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public JobResponse getJobById(Long jobId) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        JobEntity job = jobRepository
                .findByJobIdAndRecruiter(jobId, recruiter)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        return mapToResponse(job);
    }

    @Override
    public JobResponse updateJob(Long jobId, JobUpdateRequest request) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        JobEntity job = jobRepository
                .findByJobIdAndRecruiter(jobId, recruiter)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            job.setTitle(request.getTitle());
        }

        if (request.getDescription() != null &&
                !request.getDescription().isBlank()) {
            job.setDescription(request.getDescription());
        }

        if (request.getCompanyName() != null &&
                !request.getCompanyName().isBlank()) {
            job  .setCompanyName(request.getCompanyName());
        }

        if (request.getLocation() != null &&
                !request.getLocation().isBlank()) {
            job.setLocation(request.getLocation());
        }

        if (request.getEmploymentType() != null) {
            job.setEmploymentType(request.getEmploymentType());
        }

        if (request.getExperienceMin() != null) {
            job.setExperienceMin(request.getExperienceMin());
        }

        if (request.getExperienceMax() != null) {
            job.setExperienceMax(request.getExperienceMax());
        }

        if (request.getSalaryMin() != null) {
            job.setSalaryMin(request.getSalaryMin());
        }

        if (request.getSalaryMax() != null) {
            job.setSalaryMax(request.getSalaryMax());
        }

        if (request.getSkills() != null &&
                !request.getSkills().isBlank()) {
            job.setSkills(request.getSkills());
        }

        validateExperience(job.getExperienceMin(), job.getExperienceMax());

        validateSalary(job.getSalaryMin(), job.getSalaryMax());

        JobEntity updatedJob = jobRepository.save(job);

        return mapToResponse(updatedJob);
    }

    @Override
    public void deleteJob(Long jobId) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        JobEntity job = jobRepository
                .findByJobIdAndRecruiter(jobId, recruiter)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        jobRepository.delete(job);
    }

    @Override
    public JobResponse closeJob(Long jobId) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        JobEntity job = jobRepository
                .findByJobIdAndRecruiter(jobId, recruiter)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        job.setStatus(JobStatus.CLOSED);

        JobEntity updatedJob = jobRepository.save(job);

        return mapToResponse(updatedJob);
    }

    private UserEntity getAuthenticatedRecruiter() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    private void validateExperience(Integer experienceMin, Integer experienceMax) {

        if (experienceMin != null &&
                experienceMax != null &&
                experienceMin > experienceMax) {

            throw new IllegalArgumentException(
                    "Minimum experience cannot exceed maximum experience"
            );
        }
    }

    private void validateSalary(Long salaryMin, Long salaryMax) {

        if (salaryMin != null &&
                salaryMax != null &&
                salaryMin > salaryMax) {

            throw new IllegalArgumentException(
                    "Minimum salary cannot exceed maximum salary"
            );
        }
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