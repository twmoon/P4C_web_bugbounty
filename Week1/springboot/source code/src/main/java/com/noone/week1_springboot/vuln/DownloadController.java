package com.noone.week1_springboot.vuln;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/vuln/download")
public class DownloadController {

    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam String file) {
        try {
            // 적절한 필터링 없이 입력값을 그대로 받아서 path traversal 취약점 발생
            File downloadFile = new File(file);

            if (!downloadFile.exists()) {
                return ResponseEntity.badRequest().body("File not found.");
            }

            Path path = Paths.get(downloadFile.getAbsolutePath());
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFile.getName() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }
    }
}