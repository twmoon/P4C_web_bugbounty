package com.noone.week1_springboot;

import com.noone.week1_springboot.entity.User;
import com.noone.week1_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vuln/sqli")
public class SQLiController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            // 적절한 필터링 없이 입력값을 그대로 받아서 SQL Injection 취약점 발생
            List<User> users = userService.findByUsernameAndPasswordUnsafe(username, password);

            if (users != null && !users.isEmpty()) {
                StringBuilder result = new StringBuilder("<h2>Login Successful</h2>");
                for (User user : users) {
                    result.append("ID: ").append(user.getId())
                            .append(" - Username: ").append(user.getUsername())
                            .append(" - Password: ").append(user.getPassword())
                            .append("<br>");
                }
                return ResponseEntity.ok(result.toString());
            } else {
                return ResponseEntity.ok("<p>Login failed: Invalid username or password.</p>");
            }
        } catch (Exception e) {
            //쿼리 에러 그대로 출력하므로서 사용자가 쿼리 구조 파악 가능
            return ResponseEntity.ok("<p>Query failed: " + e.getMessage() + "</p>");
        }
    }
}