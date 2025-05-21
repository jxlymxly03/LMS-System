// src/main/java/com/lms/lmsbackend/service/VideoStorageService.java
package com.lms.lmsbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Service
public class VideoStorageService {
    private final Path root;

    public VideoStorageService(@Value("${video.storage.location}") String storageLocation) {
        this.root = Paths.get(storageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage dir", e);
        }
    }

    /** Saves the file and returns the URL path (e.g. /videos/abc.mp4) */
    public String store(MultipartFile file) {
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + ext;
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return "/videos/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
