package com.noone.week1_springboot.secure;

import com.noone.week1_springboot.entity.User;
import com.noone.week1_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController("secureSQLiController")
@RequestMapping("/secure/sqli")
public class SQLiController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            // Prepared Statement를 사용하여 SQL 인젝션 방지
            List<User> users = userService.findByUsernameAndPasswordSafe(username, password);

            if (users != null && !users.isEmpty()) {
                StringBuilder result = new StringBuilder("<h2>Login Successful</h2>");
                for (User user : users) {
                    // XSS 방지를 위한 HTML 이스케이프 처리
                    result.append("ID: ").append(user.getId())
                            .append(" - Username: ").append(HtmlUtils.htmlEscape(user.getUsername()))
                            .append(" - Password: ").append(HtmlUtils.htmlEscape(user.getPassword()))
                            .append("<br>");
                }
                return ResponseEntity.ok(result.toString());
            } else {
                return ResponseEntity.ok("<p>Login failed: Invalid username or password.</p>");
            }
        } catch (Exception e) {
            // 에러 메시지 노출 최소화로 SQL 구조 유출 방지
            return ResponseEntity.ok("<p>Query failed.</p>");
        }
    }
}