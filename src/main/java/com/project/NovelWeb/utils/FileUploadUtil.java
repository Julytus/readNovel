package com.project.NovelWeb.utils;

import com.project.NovelWeb.exceptions.MaximumMemoryExceededException;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.models.entities.novel.Novel;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class FileUploadUtil {

    public static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static void validateImageFile(MultipartFile file) throws IOException {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new MaximumMemoryExceededException("Image must be smaller than 5MB");
        }
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("invalid image format.");
        }

        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            throw new UnsupportedEncodingException("Payload must be images");
        }
    }

    public static <T> void updateImage(T entity, MultipartFile file, String uploadsFolder, Long id) throws IOException {
        validateImageFile(file);

        String fileName = id + "_"+UUID.randomUUID() + "_" + StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
        java.nio.file.Path uploadDir = Paths.get(uploadsFolder);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        java.nio.file.Path destination = Paths.get(uploadDir.toString(), fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        if (entity instanceof User) {
            ((User) entity).setAvatar(fileName);
        } else if (entity instanceof Novel) {
            ((Novel) entity).setImageUrl(fileName);
        }

    }
}