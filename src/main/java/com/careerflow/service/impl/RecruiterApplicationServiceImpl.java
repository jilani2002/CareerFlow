package com.careerflow.service.impl;

import com.careerflow.dto.RecruiterApplicationResponse;
import com.careerflow.entity.ApplicationEntity;
import com.careerflow.entity.ApplicationStatus;
import com.careerflow.entity.JobEntity;
import com.careerflow.entity.UserEntity;
import com.careerflow.entity.UserRole;
import com.careerflow.exception.ApplicationException;
import com.careerflow.exception.JobNotFoundException;
import com.careerflow.exception.UserNotFoundException;
import com.careerflow.repository.ApplicationRepository;
import com.careerflow.repository.JobRepository;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.RecruiterApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruiterApplicationServiceImpl
        implements RecruiterApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public RecruiterApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterApplicationResponse> getApplicationsForJob(Long jobId) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        JobEntity job = jobRepository.findByJobIdAndRecruiter(
                        jobId,
                        recruiter)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        return applicationRepository
                .findByJobOrderByAppliedAtDesc(job)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecruiterApplicationResponse getApplicationById(Long applicationId) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        ApplicationEntity application =
                applicationRepository
                        .findByApplicationIdAndJobRecruiter(
                                applicationId,
                                recruiter)
                        .orElseThrow(() -> new ApplicationException("Application not found"));

        return mapToResponse(application);
    }

    @Override
    public RecruiterApplicationResponse shortlistApplication(Long applicationId) {

        ApplicationEntity application = getApplicationForRecruiter(applicationId);

        validateStatus(application, ApplicationStatus.APPLIED);

        application.setStatus(ApplicationStatus.SHORTLISTED);

        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    public RecruiterApplicationResponse rejectApplication(Long applicationId) {

        ApplicationEntity application = getApplicationForRecruiter(applicationId);

        ApplicationStatus currentStatus = application.getStatus();

        if (currentStatus != ApplicationStatus.APPLIED
                && currentStatus != ApplicationStatus.SHORTLISTED
                && currentStatus != ApplicationStatus.INTERVIEW) {

            throw new ApplicationException("Application cannot be rejected in its current status");
        }

        application.setStatus(ApplicationStatus.REJECTED);

        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    public RecruiterApplicationResponse moveToInterview(Long applicationId) {

        ApplicationEntity application = getApplicationForRecruiter(applicationId);

        validateStatus(application, ApplicationStatus.SHORTLISTED);

        application.setStatus(ApplicationStatus.INTERVIEW);

        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    public RecruiterApplicationResponse selectApplication(Long applicationId) {

        ApplicationEntity application = getApplicationForRecruiter(applicationId);

        validateStatus(application, ApplicationStatus.INTERVIEW);

        application.setStatus(ApplicationStatus.SELECTED);

        return mapToResponse(applicationRepository.save(application));
    }

    private ApplicationEntity getApplicationForRecruiter(Long applicationId) {

        UserEntity recruiter = getAuthenticatedRecruiter();

        return applicationRepository
                .findByApplicationIdAndJobRecruiter(applicationId, recruiter)
                .orElseThrow(() -> new ApplicationException("Application not found"));
    }

    private UserEntity getAuthenticatedRecruiter() {

        String email = getAuthenticatedUserEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != UserRole.RECRUITER) {
            throw new ApplicationException("Only recruiters can manage applications");
        }

        return user;
    }

    private String getAuthenticatedUserEmail() {

        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getName();
    }

    private void validateStatus(ApplicationEntity application, ApplicationStatus expectedStatus) {

        if (application.getStatus() != expectedStatus) {
            throw new ApplicationException("Application must be in " + expectedStatus + " status");
        }
    }

    private RecruiterApplicationResponse mapToResponse(ApplicationEntity application) {

        RecruiterApplicationResponse response = new RecruiterApplicationResponse();

        response.setApplicationId(application.getApplicationId());
        response.setJobId(application.getJob().getJobId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompanyName(application.getJob().getCompanyName());
        response.setCandidateId(application.getCandidate().getUserId());
        response.setCandidateName(application.getCandidate().getFirstName() + " " + application.getCandidate().getLastName());
        response.setCandidateEmail(application.getCandidate().getEmail());
        response.setResumeId(application.getResume().getResumeId());
        response.setResumeTitle(application.getResume().getTitle());
        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        response.setUpdatedAt(application.getUpdatedAt());
        return response;
    }
}