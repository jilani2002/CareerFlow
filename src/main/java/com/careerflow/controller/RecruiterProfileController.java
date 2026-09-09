package com.careerflow.controller;

import com.careerflow.dto.RecruiterProfileResponse;
import com.careerflow.dto.RecruiterProfileUpdateRequest;
import com.careerflow.service.RecruiterProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiters/profile")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService) {
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping
    public ResponseEntity<RecruiterProfileResponse> getProfile() {
        return ResponseEntity.ok(recruiterProfileService.getProfile());
    }

    @PutMapping
    public ResponseEntity<RecruiterProfileResponse> updateProfile(
            @Valid @RequestBody RecruiterProfileUpdateRequest request) {

        return ResponseEntity.ok(recruiterProfileService.updateProfile(request));
    }
}