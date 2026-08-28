package com.careerflow.controller;

import com.careerflow.dto.ResumeResponse;
import com.careerflow.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeResponse> uploadResume(@RequestParam String title,
                                                       @RequestParam MultipartFile file) {
        ResumeResponse response = resumeService.uploadResume(title, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getResumes() {
        return ResponseEntity.ok(resumeService.getResumes());
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeResponse> getResumeById(@PathVariable Long resumeId) {
        return ResponseEntity.ok(resumeService.getResumeById(resumeId));
    }

    @PutMapping("/{resumeId}")
    public ResponseEntity<ResumeResponse> updateResume(
            @PathVariable Long resumeId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) MultipartFile file) {
        return ResponseEntity.ok(resumeService.updateResume(resumeId, title, file));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {

        resumeService.deleteResume(resumeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{resumeId}/primary")
    public ResponseEntity<ResumeResponse> setPrimaryResume(@PathVariable Long resumeId) {
        return ResponseEntity.ok(resumeService.setPrimaryResume(resumeId));
    }
}