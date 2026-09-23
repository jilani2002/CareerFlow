package com.careerflow.dto;

import com.careerflow.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {

    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Long resumeId;
    private String resumeTitle;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}