package com.noone.week1_springboot;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vuln/xss")
public class XssController {

    @GetMapping("/echo")
    public String echoInput(@RequestParam(required = false) String input, HttpServletResponse response) {
        // 쿠키 설정
        Cookie cookie = new Cookie("xss", "flag{xss_is_fun}");
        cookie.setPath("/");
        cookie.setHttpOnly(false); // XSS 공격으로 접근 가능하도록 HttpOnly 비활성화
        response.addCookie(cookie);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"ko\">");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Vulnerable XSS Page</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>XSS Test Page</h1>");
        html.append("<form method=\"GET\" action=\"\">");
        html.append("<label for=\"input\"> Input text:</label>");
        html.append("<input type=\"text\" id=\"input\" name=\"input\" required><br><br>");
        html.append("<input type=\"submit\" value=\"Submit\">");
        html.append("</form>");

        if (input != null && !input.isEmpty()) {
            // 적절한 필터링 없이 입력값을 그대로 받아서 xss 취약점 발생 가능
            html.append("<p>You entered: " + input + "</p>");
        }

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}