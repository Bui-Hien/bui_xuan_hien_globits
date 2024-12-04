package com.buihien.demo.services;

import com.buihien.demo.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Slf4j
@Component
public class UploadImg {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // Hàm upload tệp
    public static String uploadImg(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResourceNotFoundException("No file uploaded");
        }

        try {
            File uploadDirFile = new File(UPLOAD_DIR);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = uuid + fileExtension;
            String filePath = UPLOAD_DIR + fileName;

            file.transferTo(new File(filePath));

            URI fileUri = URI.create(fileName);
            return fileUri.toString();
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new ResourceNotFoundException("Could not upload file");
        }
    }

    // Hàm xóa tệp
    public static void deleteFile(String fileName) {
        String filePath = UPLOAD_DIR + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                log.info("File {} deleted successfully", fileName);
            } else {
                log.error("Failed to delete file {}", fileName);
                throw new ResourceNotFoundException("Could not delete file");
            }
        } else {
            log.error("File {} not found", fileName);
            throw new ResourceNotFoundException("File not found to delete");
        }
    }
}
