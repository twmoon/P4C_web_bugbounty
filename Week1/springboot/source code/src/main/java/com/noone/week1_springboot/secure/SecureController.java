package com.noone.week1_springboot.secure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecureController {

    @GetMapping("/secure/upload")
    public String uploadSecure() {
        return "redirect:/secure/upload.html";
    }

    @GetMapping("/secure/download")
    public String downloadSecure() {
        return "redirect:/secure/download.html";
    }

    @GetMapping("/secure/xss")
    public String xssSecure() {
        return "redirect:/secure/xss.html";
    }

    @GetMapping("/secure/sqli")
    public String sqliSecure() {
        return "redirect:/secure/sqli.html";
    }
}