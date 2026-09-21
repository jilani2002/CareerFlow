package com.careerflow.controller;

import com.careerflow.dto.JobResponse;
import com.careerflow.entity.EmploymentType;
import com.careerflow.service.JobDiscoveryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobDiscoveryController {

    private final JobDiscoveryService jobDiscoveryService;

    public JobDiscoveryController(JobDiscoveryService jobDiscoveryService) {
        this.jobDiscoveryService = jobDiscoveryService;
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> searchJobs(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String location,

            @RequestParam(required = false)
            EmploymentType employmentType,

            @RequestParam(required = false)
            Integer experienceMin,

            @RequestParam(required = false)
            Integer experienceMax,

            @RequestParam(required = false)
            Long salaryMin,

            @RequestParam(required = false)
            Long salaryMax,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                jobDiscoveryService.searchJobs(
                        keyword,
                        location,
                        employmentType,
                        experienceMin,
                        experienceMax,
                        salaryMin,
                        salaryMax,
                        page,
                        size
                )
        );
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobDiscoveryService.getJobById(jobId));
    }
}