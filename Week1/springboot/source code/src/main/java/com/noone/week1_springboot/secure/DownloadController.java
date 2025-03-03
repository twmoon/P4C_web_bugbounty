package com.noone.week1_springboot.secure;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@RestController("secureDownloadController")
@RequestMapping("/secure/download")
public class DownloadController {

    private final String BASE_DIR = "src/main/resources/downloads/";

    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam String file) {
        try {
            // 경로 조작 방지: .. 등의 상위 디렉토리 참조 차단
            if (file.contains("..")) {
                return ResponseEntity.badRequest().body("Invalid file path.");
            }

            // 정규화된 경로를 얻어 BASE_DIR 내부인지 검증
            Path basePath = Paths.get(BASE_DIR).normalize();
            Path filePath = basePath.resolve(file).normalize();

            // 파일 경로가 BASE_DIR로 시작하는지 확인 (디렉토리 트래버설 방지)
            if (!filePath.startsWith(basePath) || !Files.exists(filePath)) {
                return ResponseEntity.badRequest().body("File not found or access denied.");
            }

            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process request.");
        }
    }
}