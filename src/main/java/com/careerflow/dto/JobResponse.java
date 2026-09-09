package com.careerflow.dto;

import com.careerflow.entity.EmploymentType;
import com.careerflow.entity.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {

    private Long jobId;

    private Long recruiterId;

    private String companyName;

    private String title;

    private String description;

    private String location;

    private EmploymentType employmentType;

    private Integer experienceMin;

    private Integer experienceMax;

    private Long salaryMin;

    private Long salaryMax;

    private String skills;

    private JobStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}