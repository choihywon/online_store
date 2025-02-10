package com.example.bookstore.user.controller;

import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    // 홈 API
    @GetMapping
    public String home() {
        return "환영합니다! 로그인 후 서비스를 이용하세요.";
    }

    //회원가입 안내 API
    @GetMapping("/join")
    public String joinPage() {
        return "회원가입을 진행하세요.";
    }

    //로그인 안내 API
    @GetMapping("/login")
    public String loginPage() {
        return "로그인을 진행하세요.";
    }

    //로그아웃 API
    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "로그아웃 되었습니다.";
    }
}