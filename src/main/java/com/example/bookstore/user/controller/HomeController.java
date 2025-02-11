package com.example.bookstore.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** 🚀 홈 페이지 */
    @GetMapping("/")
    public String index() {
        return "index"; //
    }
}
