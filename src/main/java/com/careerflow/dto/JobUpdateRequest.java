package com.careerflow.dto;

import com.careerflow.entity.EmploymentType;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobUpdateRequest {

    private String title;

    private String description;

    private String companyName;

    private String location;

    private EmploymentType employmentType;

    @Min(value = 0, message = "Minimum experience cannot be negative")
    private Integer experienceMin;

    @Min(value = 0, message = "Maximum experience cannot be negative")
    private Integer experienceMax;

    @Min(value = 0, message = "Minimum salary cannot be negative")
    private Long salaryMin;

    @Min(value = 0, message = "Maximum salary cannot be negative")
    private Long salaryMax;

    private String skills;
}