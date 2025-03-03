package com.noone.week1_springboot.secure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController("secureXssController")
@RequestMapping("/secure/xss")
public class XssController {

    @GetMapping("/echo")
    public String echoInput(@RequestParam(required = false) String input, HttpServletResponse response) {
        // HttpOnly 속성을 true로 설정하여 자바스크립트에서 쿠키 접근 방지
        Cookie cookie = new Cookie("xss", "flag{xss_is_fun}");
        cookie.setPath("/");
        cookie.setHttpOnly(true); // XSS로 쿠키 접근 방지
        response.addCookie(cookie);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"UTF-8\">");
        html.append("<title>Secure XSS Page</title></head><body>");
        html.append("<h1>XSS Test Page</h1>");
        html.append("<p>Update: No funky letters or symbols is allowed into query</p>");
        html.append("<form method=\"GET\" action=\"\">");
        html.append("<label for=\"input\"> Input text:</label>");
        html.append("<input type=\"text\" id=\"input\" name=\"input\" required><br><br>");
        html.append("<input type=\"submit\" value=\"Submit\">");
        html.append("</form>");

        if (input != null && !input.isEmpty()) {
            // HTML 이스케이프 처리로 XSS 방지
            String safeInput = HtmlUtils.htmlEscape(input);
            html.append("<p>You entered: ").append(safeInput).append("</p>");
        }

        html.append("</body></html>");
        return html.toString();
    }
}