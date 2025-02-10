package com.example.bookstore.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    // 홈(index) 페이지 렌더링
    @GetMapping("/")
    public String index() {
        return "index";  // templates/index.html을 반환
    }
}