package com.careerflow.service.impl;

import com.careerflow.dto.ApplicationCreateRequest;
import com.careerflow.dto.ApplicationResponse;
import com.careerflow.entity.ApplicationEntity;
import com.careerflow.entity.ApplicationStatus;
import com.careerflow.entity.JobEntity;
import com.careerflow.entity.JobStatus;
import com.careerflow.entity.ResumeEntity;
import com.careerflow.entity.UserEntity;
import com.careerflow.exception.ApplicationException;
import com.careerflow.exception.JobNotFoundException;
import com.careerflow.exception.ResumeNotFoundException;
import com.careerflow.exception.UserNotFoundException;
import com.careerflow.repository.ApplicationRepository;
import com.careerflow.repository.JobRepository;
import com.careerflow.repository.ResumeRepository;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.ApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            ResumeRepository resumeRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
    }

    @Override
    public ApplicationResponse applyForJob(ApplicationCreateRequest request) {

        UserEntity candidate = getAuthenticatedUser();

        JobEntity job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new ApplicationException("Candidate can apply only to an open job");
        }

        boolean alreadyApplied = applicationRepository.existsByCandidateAndJob(candidate, job);

        if (alreadyApplied) {
            throw new ApplicationException(
                    "Candidate has already applied for this job");
        }

        ResumeEntity resume = resumeRepository.findByResumeIdAndUser(request.getResumeId(), candidate)
                .orElseThrow(() -> new ResumeNotFoundException("Resume not found"));

        ApplicationEntity application = new ApplicationEntity();

        application.setCandidate(candidate);
        application.setJob(job);
        application.setResume(resume);
        application.setStatus(ApplicationStatus.APPLIED);

        ApplicationEntity savedApplication = applicationRepository.save(application);

        return mapToResponse(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {

        UserEntity candidate = getAuthenticatedUser();

        return applicationRepository
                .findByCandidateOrderByAppliedAtDesc(candidate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getMyApplication(Long applicationId) {

        UserEntity candidate = getAuthenticatedUser();

        ApplicationEntity application = applicationRepository
                        .findByApplicationIdAndCandidate(
                                applicationId,
                                candidate)
                        .orElseThrow(() -> new ApplicationException("Application not found"));

        return mapToResponse(application);
    }

    @Override
    public ApplicationResponse withdrawApplication(Long applicationId) {

        UserEntity candidate = getAuthenticatedUser();

        ApplicationEntity application = applicationRepository
                        .findByApplicationIdAndCandidate(applicationId, candidate)
                        .orElseThrow(() -> new ApplicationException("Application not found"));

        if (application.getStatus() != ApplicationStatus.APPLIED) {
            throw new ApplicationException("Only an active application can be withdrawn");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);

        ApplicationEntity updatedApplication = applicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    private UserEntity getAuthenticatedUser() {

        String email = getAuthenticatedUserEmail();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private String getAuthenticatedUserEmail() {

        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        return authentication.getName();
    }

    private ApplicationResponse mapToResponse(ApplicationEntity application) {

        ApplicationResponse response = new ApplicationResponse();
        response.setApplicationId(application.getApplicationId());
        response.setJobId(application.getJob().getJobId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompanyName(application.getJob().getCompanyName());
        response.setResumeId(application.getResume().getResumeId());
        response.setResumeTitle(application.getResume().getTitle());
        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        response.setUpdatedAt(application.getUpdatedAt());
        return response;
    }
}