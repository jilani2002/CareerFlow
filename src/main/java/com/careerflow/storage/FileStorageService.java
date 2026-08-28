package com.careerflow.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(MultipartFile file, Long userId);

    void delete(String storageKey);
}