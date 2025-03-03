package com.noone.week1_springboot;

import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/vuln/upload-file")
public class UploadController {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 취약점: 파일 이름 검증이나 확장자 필터링 없이 그대로 저장
            // 악성 .jsp, .php 등 파일 업로드 가능
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + file.getOriginalFilename();
            String message = "File uploaded successfully: <a href='" + fileUrl + "'>" + file.getOriginalFilename() + "</a>";

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(getUploadHtml(message));
        } catch (IOException e) {
            String error = "Failed to upload file: " + e.getMessage();
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_HTML)
                    .body(getUploadHtml(error));
        }
    }

    @GetMapping("/content")
    public ResponseEntity<?> getFileContent(@RequestParam String file) {
        try {
            // 취약점: 파일 경로 검증 없이 그대로 사용
            Path filePath = Paths.get(UPLOAD_DIR, file);
            String content = new String(Files.readAllBytes(filePath));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to read file: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> showUploadForm() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(getUploadHtml(null));
    }

    private String getUploadHtml(String message) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Vulnerable File Upload Page</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>File Upload Test Page</h1>");
        html.append("<p>Read the file \"upload.flag\"</p>");
        html.append("<p>Hint: It is located in the private directory that cannot be accessed directly via web</p>");
        html.append("<form method=\"POST\" enctype=\"multipart/form-data\" action=\"/vuln/upload/file\">");
        html.append("<label for=\"file\">Upload a file:</label>");
        html.append("<input type=\"file\" id=\"file\" name=\"file\" required><br><br>");
        html.append("<input type=\"submit\" value=\"Upload\">");
        html.append("</form>");

        if (message != null) {
            html.append("<p>" + message + "</p>");
        }

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}