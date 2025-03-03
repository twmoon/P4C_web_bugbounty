package com.noone.week1_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class Week1SpringbootApplication {

    public static void main(String[] args) {
        createFlagsAndDirectories();
        SpringApplication.run(Week1SpringbootApplication.class, args);
    }

    private static void createFlagsAndDirectories() {
        try {
            File downloadsDir = new File("src/main/resources/downloads");
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            File fakeFlag = new File(downloadsDir, "download.flag");
            try (FileWriter writer = new FileWriter(fakeFlag)) {
                writer.write("Try harder.. kkk");
            }

            File realFlag = new File("download.flag");
            try (FileWriter writer = new FileWriter(realFlag)) {
                writer.write("flag{download_is_fun}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File uploadDir = new File("src/main/resources/static/uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File privateDir = new File("src/main/resources/static/uploads/private");
            if (!privateDir.exists()) {
                privateDir.mkdirs();
                privateDir.setExecutable(true, true);
                privateDir.setReadable(true, true);
                privateDir.setWritable(true, true);
            }

            File uploadFlag = new File(privateDir, "upload.flag");
            try (FileWriter writer = new FileWriter(uploadFlag)) {
                writer.write("flag{upload_is_fun}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}