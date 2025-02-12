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


    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @PostMapping("/join")
    public String joinUser(@ModelAttribute JoinUserDto joinUserDto) {
        userService.joinUser(joinUserDto);
        return "redirect:/users/login"; // 회원가입 후 로그인 페이지로 이동
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.checkDuplicateEmail(email);

        if (isDuplicate) {
            System.out.println("이미 사용 중인 이메일: " + email);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"이미 사용 중인 이메일입니다.\"}");
        } else {
            System.out.println("사용 가능한 이메일: " + email);
            return ResponseEntity.ok("{\"message\":\"사용 가능한 이메일입니다.\"}");
        }
    }




    @GetMapping("/mypage")
    public String myPage(Model model, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login"; // 로그인 안 한 경우 로그인 페이지로 이동
        }

        String email = authentication.getName();
        try {
            UserDto userDto = userService.findByEmail(email);
            model.addAttribute("user", userDto);
            return "users/mypage";
        } catch (Exception e) {
            System.err.println(" 마이페이지 오류: " + e.getMessage());
            return "redirect:/users/login"; // 예외 발생 시 로그인 페이지로 이동
        }
    }


    @PostMapping("/mypage/edit")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto updateUserDto, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"로그인이 필요합니다.\"}");
        }

        String email = authentication.getName();
        userService.updateUser(email, updateUserDto);

        return ResponseEntity.ok("{\"message\":\"회원 정보가 수정되었습니다.\"}");
    }


    @PostMapping("/delete")
    public String deleteUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login";
        }

        String email = authentication.getName();
        userService.deactivateUser(email);
        return "redirect:/";
    }
}
