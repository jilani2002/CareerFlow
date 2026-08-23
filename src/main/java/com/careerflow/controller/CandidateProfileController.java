package com.careerflow.controller;

import com.careerflow.dto.CandidateProfileResponse;
import com.careerflow.dto.CandidateProfileUpdateRequest;
import com.careerflow.service.CandidateProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(CandidateProfileService candidateProfileService) {
        this.candidateProfileService = candidateProfileService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> getProfile() {
        return ResponseEntity.ok(candidateProfileService.getProfile());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> updateProfile(
            @Valid @RequestBody CandidateProfileUpdateRequest request) {
        return ResponseEntity.ok(
                candidateProfileService.updateProfile(request)
        );
    }
}