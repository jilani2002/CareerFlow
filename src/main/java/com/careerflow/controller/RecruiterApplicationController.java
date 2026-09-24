package com.careerflow.controller;

import com.careerflow.dto.RecruiterApplicationResponse;
import com.careerflow.service.RecruiterApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiters")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterApplicationController {

    private final RecruiterApplicationService recruiterApplicationService;

    public RecruiterApplicationController(RecruiterApplicationService recruiterApplicationService) {
        this.recruiterApplicationService = recruiterApplicationService;
    }

    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<List<RecruiterApplicationResponse>>
    getApplicationsForJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(recruiterApplicationService.getApplicationsForJob(jobId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<RecruiterApplicationResponse>
    getApplicationById(@PathVariable Long applicationId) {
        return ResponseEntity.ok(recruiterApplicationService.getApplicationById(applicationId));
    }

    @PutMapping("/applications/{applicationId}/shortlist")
    public ResponseEntity<RecruiterApplicationResponse> shortlistApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(recruiterApplicationService.shortlistApplication(applicationId));
    }

    @PutMapping("/applications/{applicationId}/reject")
    public ResponseEntity<RecruiterApplicationResponse> rejectApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(recruiterApplicationService.rejectApplication(applicationId));
    }

    @PutMapping("/applications/{applicationId}/interview")
    public ResponseEntity<RecruiterApplicationResponse> moveToInterview(@PathVariable Long applicationId) {
        return ResponseEntity.ok(recruiterApplicationService.moveToInterview(applicationId));
    }

    @PutMapping("/applications/{applicationId}/select")
    public ResponseEntity<RecruiterApplicationResponse> selectApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(recruiterApplicationService.selectApplication(applicationId));
    }
}