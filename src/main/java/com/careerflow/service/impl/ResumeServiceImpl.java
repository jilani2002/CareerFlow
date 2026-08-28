package com.careerflow.service.impl;

import com.careerflow.dto.ResumeResponse;
import com.careerflow.entity.ResumeEntity;
import com.careerflow.entity.UserEntity;
import com.careerflow.exception.ResumeNotFoundException;
import com.careerflow.exception.UserNotFoundException;
import com.careerflow.repository.ResumeRepository;
import com.careerflow.repository.UserRepository;
import com.careerflow.service.ResumeService;
import com.careerflow.storage.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ResumeServiceImpl(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResumeResponse uploadResume(String title, MultipartFile file) {

        validateTitle(title);
        validateFile(file);

        String email = getAuthenticatedUserEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String storageKey = fileStorageService.store(file, user.getUserId());

        ResumeEntity resume = new ResumeEntity();

        resume.setUser(user);
        resume.setTitle(title);
        resume.setFileName(file.getOriginalFilename());
        resume.setFileType(file.getContentType());
        resume.setFileSize(file.getSize());
        resume.setStorageKey(storageKey);
        resume.setPrimaryResume(false);

        ResumeEntity savedResume = resumeRepository.save(resume);
        return mapToResponse(savedResume);
    }

    @Override
    public List<ResumeResponse> getResumes() {

        String email = getAuthenticatedUserEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return resumeRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ResumeResponse getResumeById(Long resumeId) {

        UserEntity user = getAuthenticatedUser();

        ResumeEntity resume = resumeRepository
                .findByResumeIdAndUser(resumeId, user)
                .orElseThrow(() -> new ResumeNotFoundException("Resume not found"));
        return mapToResponse(resume);
    }

    @Override
    public ResumeResponse updateResume(Long resumeId, String title, MultipartFile file) {

        UserEntity user = getAuthenticatedUser();

        ResumeEntity resume = resumeRepository
                .findByResumeIdAndUser(resumeId, user)
                .orElseThrow(() ->
                        new ResumeNotFoundException("Resume not found"));

        if (title != null && !title.isBlank()) {
            resume.setTitle(title);
        }

        if (file != null && !file.isEmpty()) {

            validateFile(file);

            String oldStorageKey = resume.getStorageKey();

            String newStorageKey =
                    fileStorageService.store(file, user.getUserId());
            try {

                resume.setStorageKey(newStorageKey);
                resume.setFileName(file.getOriginalFilename());
                resume.setFileType(file.getContentType());
                resume.setFileSize(file.getSize());

                ResumeEntity updatedResume = resumeRepository.save(resume);
                fileStorageService.delete(oldStorageKey);

                return mapToResponse(updatedResume);
            } catch (Exception exception) {
                fileStorageService.delete(newStorageKey);
                throw exception;
            }
        }
        ResumeEntity updatedResume = resumeRepository.save(resume);
        return mapToResponse(updatedResume);
    }

    @Override
    public void deleteResume(Long resumeId) {

        UserEntity user = getAuthenticatedUser();

        ResumeEntity resume = resumeRepository
                .findByResumeIdAndUser(resumeId, user)
                .orElseThrow(() ->
                        new ResumeNotFoundException("Resume not found"));

        fileStorageService.delete(resume.getStorageKey());

        resumeRepository.delete(resume);
    }

    @Override
    public ResumeResponse setPrimaryResume(Long resumeId) {

        UserEntity user = getAuthenticatedUser();

        ResumeEntity resume = resumeRepository
                .findByResumeIdAndUser(resumeId, user)
                .orElseThrow(() -> new ResumeNotFoundException("Resume not found"));

        resumeRepository
                .findByUserAndPrimaryResumeTrue(user)
                .ifPresent(primaryResume -> {
                    primaryResume.setPrimaryResume(false);
                    resumeRepository.save(primaryResume);
                });

        resume.setPrimaryResume(true);

        ResumeEntity updatedResume = resumeRepository.save(resume);

        return mapToResponse(updatedResume);
    }

    private void validateTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Resume title is required");
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Resume file size must not exceed 5 MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf")
                        && !contentType.equals("application/msword")
                        && !contentType.equals(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

            throw new IllegalArgumentException("Only PDF, DOC and DOCX files are allowed");
        }
    }

    private UserEntity getAuthenticatedUser() {

        String email = getAuthenticatedUserEmail();
        return userRepository.findByEmail(email).orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    private String getAuthenticatedUserEmail() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private ResumeResponse mapToResponse(ResumeEntity resume) {

        ResumeResponse response = new ResumeResponse();
        response.setResumeId(resume.getResumeId());
        response.setTitle(resume.getTitle());
        response.setFileName(resume.getFileName());
        response.setFileType(resume.getFileType());
        response.setFileSize(resume.getFileSize());
        response.setPrimaryResume(resume.getPrimaryResume());
        response.setCreatedAt(resume.getCreatedAt());
        response.setUpdatedAt(resume.getUpdatedAt());
        return response;
    }
}