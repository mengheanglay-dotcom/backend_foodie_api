package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only JPEG, PNG, WEBP, and GIF images are allowed"
            );
        }

        try {
            Path folderPath = Path.of(uploadDir, folder).toAbsolutePath().normalize();
            Files.createDirectories(folderPath);

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
            String extension = getExtension(originalFilename);
            String filename = UUID.randomUUID() + extension;

            Path targetPath = folderPath.resolve(filename);
            file.transferTo(targetPath.toFile());

            return "/uploads/" + folder + "/" + filename;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload image"
            );
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String relativePath = extractRelativePath(fileUrl);

            if (relativePath == null || relativePath.isBlank()) {
                return;
            }

            Path uploadRootPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path targetPath = uploadRootPath.resolve(relativePath).normalize();

            if (!targetPath.startsWith(uploadRootPath)) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            boolean deleted = Files.deleteIfExists(targetPath);

            System.out.println("DELETE TARGET PATH = " + targetPath);
            System.out.println("DELETED = " + deleted);
        } catch (IOException exception) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete old image");
        }
    }

    private String extractRelativePath(String fileUrl) {
        String path = fileUrl;

        try {
            if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
                path = URI.create(fileUrl).getPath();
            }
        } catch (IllegalArgumentException ignored) {
            path = fileUrl;
        }

        path = path.replace("\\", "/");

        int uploadsIndex = path.indexOf("/uploads/");
        if (uploadsIndex >= 0) {
            path = path.substring(uploadsIndex + "/uploads/".length());
        }

        if (path.startsWith("uploads/")) {
            path = path.substring("uploads/".length());
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        return path;
    }



    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }

        return filename.substring(dotIndex);
    }
}
