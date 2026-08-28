package com.careerflow.service;

import com.careerflow.dto.ResumeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    ResumeResponse uploadResume(String title, MultipartFile file);
    List<ResumeResponse> getResumes();

    ResumeResponse getResumeById(Long resumeId);

    ResumeResponse updateResume(Long resumeId, String title, MultipartFile file);

    void deleteResume(Long resumeId);

    ResumeResponse setPrimaryResume(Long resumeId);
}