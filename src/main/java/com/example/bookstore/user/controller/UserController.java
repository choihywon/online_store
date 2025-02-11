package com.example.bookstore.user.controller;

import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.dto.LoginUserDto;
import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🚀 회원가입 페이지
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    // 🚀 회원가입 요청
    @PostMapping("/join")
    public String joinUser(@ModelAttribute JoinUserDto joinUserDto) {
        userService.joinUser(joinUserDto);
        return "redirect:/users/login"; // 회원가입 후 로그인 페이지로 이동
    }

    // 🚀 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 🚀 마이페이지
    @GetMapping("/mypage")
    public String myPage(Model model, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login"; // 로그인 안 한 경우 로그인 페이지로 이동
        }

        String email = authentication.getName();
        UserDto userDto = userService.findByEmail(email); // ❌ 여기서 예외 발생 가능

        model.addAttribute("user", userDto);
        return "users/mypage"; // 마이페이지 View 반환
    }


    // 🚀 회원 정보 수정 페이지
    @GetMapping("/edit")
    public String editUserPage(Model model, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login";
        }

        String email = authentication.getName();
        UserDto userDto = userService.findByEmail(email);
        model.addAttribute("user", userDto);
        return "user/edit"; // 회원 정보 수정 페이지
    }

    // 🚀 회원 정보 수정 요청
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute UpdateUserDto updateUserDto, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login";
        }

        String email = authentication.getName();
        userService.updateUser(email, updateUserDto);
        return "redirect:/users/mypage"; // 수정 후 마이페이지로 이동
    }

    // 🚀 회원 탈퇴 요청
    @PostMapping("/delete")
    public String deleteUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login";
        }

        String email = authentication.getName();
        userService.deactivateUser(email); // 회원 비활성화 처리
        return "redirect:/"; // 탈퇴 후 홈으로 이동
    }
}
