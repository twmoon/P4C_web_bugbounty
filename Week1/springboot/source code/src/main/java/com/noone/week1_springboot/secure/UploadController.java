package com.noone.week1_springboot.secure;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@RestController("secureUploadController")
@RequestMapping("/secure/upload")
public class UploadController {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("txt", "pdf", "jpg", "png");

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.contains("..")) { // 경로 조작 방지
                return ResponseEntity.badRequest()
                        .contentType(MediaType.TEXT_HTML)
                        .body(getUploadHtml("Invalid file name."));
            }

            // 확장자 화이트리스트 검사로 악성 파일 업로드 방지
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(fileExt)) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.TEXT_HTML)
                        .body(getUploadHtml("Only txt, pdf, jpg, png files are allowed."));
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + fileName;
            String message = "File uploaded successfully: <a href='" + fileUrl + "'>" + fileName + "</a>";

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
            if (file.contains("..")) { // 경로 조작 방지
                return ResponseEntity.badRequest().body("Invalid file path.");
            }

            // 파일 경로 검증을 통한 디렉토리 트래버설 방지
            Path filePath = Paths.get(UPLOAD_DIR, file);
            if (!filePath.normalize().startsWith(Paths.get(UPLOAD_DIR))) {
                return ResponseEntity.badRequest().body("Access denied.");
            }

            String content = new String(Files.readAllBytes(filePath));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to read file.");
        }
    }

    @GetMapping("/form")
    public ResponseEntity<?> showUploadForm() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(getUploadHtml(null));
    }

    private String getUploadHtml(String message) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Secure File Upload Page</title></head><body>");
        html.append("<h1>File Upload Test Page</h1>");
        html.append("<p>Read the file \"upload.flag\"</p>");
        html.append("<p>Hint: It is located in the private directory that cannot be accessed directly via web</p>");
        html.append("<p>Update: Only txt, pdf, jpg, png file format is allowed</p>");
        html.append("<form method=\"POST\" enctype=\"multipart/form-data\" action=\"/secure/upload/file\">");
        html.append("<label for=\"file\">Upload a file:</label>");
        html.append("<input type=\"file\" id=\"file\" name=\"file\" required><br><br>");
        html.append("<input type=\"submit\" value=\"Upload\">");
        html.append("</form>");

        if (message != null) {
            html.append("<p>").append(message).append("</p>");
        }

        html.append("</body></html>");
        return html.toString();
    }
}