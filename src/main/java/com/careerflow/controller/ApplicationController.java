package com.careerflow.controller;

import com.careerflow.dto.ApplicationCreateRequest;
import com.careerflow.dto.ApplicationResponse;
import com.careerflow.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates/applications")
@PreAuthorize("hasRole('CANDIDATE')")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> applyForJob(@Valid @RequestBody ApplicationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.applyForJob(request));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getMyApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.getMyApplication(applicationId));
    }

    @PutMapping("/{applicationId}/withdraw")
    public ResponseEntity<ApplicationResponse> withdrawApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.withdrawApplication(applicationId));
    }
}