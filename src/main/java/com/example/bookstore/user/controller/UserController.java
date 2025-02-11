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
    // 🚀 이메일 중복 체크 API
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.checkDuplicateEmail(email);
        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"이미 사용 중인 이메일입니다.\"}");
        } else {
            return ResponseEntity.ok("{\"message\":\"사용 가능한 이메일입니다.\"}");
        }
    }


    // 🚀 마이페이지
    @GetMapping("/mypage")
    public String myPage(Model model, Authentication authentication) {
        // ✅ 로그인 여부 확인
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login"; // 로그인 안 한 경우 로그인 페이지로 이동
        }

        String email = authentication.getName();
        try {
            UserDto userDto = userService.findByEmail(email);
            model.addAttribute("user", userDto);
            return "users/mypage"; // 마이페이지 View 반환
        } catch (Exception e) {
            System.err.println("🚨 마이페이지 오류: " + e.getMessage());
            return "redirect:/users/login"; // 예외 발생 시 로그인 페이지로 이동
        }
    }



    // 🚀 마이페이지에서 회원 정보 수정
    @PostMapping("/mypage/edit")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto updateUserDto, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"로그인이 필요합니다.\"}");
        }

        String email = authentication.getName();
        userService.updateUser(email, updateUserDto);

        return ResponseEntity.ok("{\"message\":\"회원 정보가 수정되었습니다.\"}");
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
