package com.careerflow.dto;

import com.careerflow.entity.EmploymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreateRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Minimum experience is required")
    @Min(value = 0, message = "Minimum experience cannot be negative")
    private Integer experienceMin;

    @NotNull(message = "Maximum experience is required")
    @Min(value = 0, message = "Maximum experience cannot be negative")
    private Integer experienceMax;

    @Min(value = 0, message = "Minimum salary cannot be negative")
    private Long salaryMin;

    @Min(value = 0, message = "Maximum salary cannot be negative")
    private Long salaryMax;

    @NotBlank(message = "Skills are required")
    private String skills;
}