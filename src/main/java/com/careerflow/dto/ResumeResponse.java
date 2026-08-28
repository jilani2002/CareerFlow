package com.careerflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private Long resumeId;
    private String title;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Boolean primaryResume;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}