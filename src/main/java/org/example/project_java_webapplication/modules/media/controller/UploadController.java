package org.example.project_java_webapplication.modules.media.controller;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.media.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("Received upload request for file: " + file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                System.out.println("Upload failed: File is empty");
                return ResponseEntity.badRequest().body("File is empty");
            }

            String fileName = fileStorageService.saveFile(file);
            String fileUrl = "/uploads/" + fileName;
            System.out.println("File saved successfully: " + fileName + " -> " + fileUrl);

            return ResponseEntity.ok(Map.of(
                "url", fileUrl,
                "fileName", fileName
            ));
        } catch (Exception e) {
            System.err.println("Upload exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload error: " + e.getMessage());
        }
    }
}