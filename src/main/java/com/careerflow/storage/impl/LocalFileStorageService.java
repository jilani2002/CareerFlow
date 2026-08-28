package com.careerflow.storage.impl;

import com.careerflow.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path storageLocation;

    public LocalFileStorageService(@Value("${file.storage.location:uploads}") String storageLocation) {

        this.storageLocation = Paths.get(storageLocation)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException exception) {
            throw new RuntimeException("Could not initialize file storage", exception);
        }
    }

    @Override
    public String store(MultipartFile file, Long userId) {

        String originalFileName = file.getOriginalFilename();

        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String storageKey = "resumes/" + userId + "/" + UUID.randomUUID() + extension;

        Path targetLocation = storageLocation.resolve(storageKey).normalize();

        if (!targetLocation.startsWith(storageLocation)) {
            throw new IllegalArgumentException("Invalid storage key");
        }
        try {

            Files.createDirectories(targetLocation.getParent());

            Files.copy(
                    file.getInputStream(),
                    targetLocation
            );
            return storageKey;
        } catch (IOException exception) {
            throw new RuntimeException("Could not store file", exception);
        }
    }

    @Override
    public void delete(String storageKey) {

        Path targetLocation = storageLocation.resolve(storageKey)
                .normalize();
        try {
            Files.deleteIfExists(targetLocation);
        } catch (IOException exception) {
            throw new RuntimeException("Could not delete file", exception);
        }
    }
}