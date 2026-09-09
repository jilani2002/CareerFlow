package com.careerflow.controller;

import com.careerflow.dto.JobCreateRequest;
import com.careerflow.dto.JobResponse;
import com.careerflow.dto.JobUpdateRequest;
import com.careerflow.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiters/jobs")
@PreAuthorize("hasRole('RECRUITER')")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobCreateRequest request) {

        JobResponse response = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getMyJobs() {

        return ResponseEntity.ok(jobService.getMyJobs());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobUpdateRequest request) {

        return ResponseEntity.ok(jobService.updateJob(jobId, request));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {

        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{jobId}/close")
    public ResponseEntity<JobResponse> closeJob(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(jobService.closeJob(jobId));
    }
}