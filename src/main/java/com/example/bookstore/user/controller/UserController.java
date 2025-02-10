package com.example.bookstore.user.controller;

import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.dto.LoginUserDto;
import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🔹 회원가입 페이지 렌더링 (join.html)
    @GetMapping("/join")  // 이제 "/join"으로 직접 접근 가능
    public String joinPage() {
        return "join";  // ✅ templates/join.html 반환
    }

    // 🔹 로그인 페이지 렌더링 (login.html)
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // templates/login.html 반환
    }

    // 🔹 마이페이지 렌더링 (mypage.html)
    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";  // templates/mypage.html 반환
    }

    @PostMapping("/signup")
    public String joinUser(@ModelAttribute JoinUserDto joinUserDto) {
        userService.joinUser(joinUserDto);
        return "redirect:/users/login";  // 회원가입 후 로그인 페이지로 이동
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginUserDto loginUserDto) {
        UserDto userDto = userService.loginUser(loginUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    // 🔹 이메일 중복 확인 API
    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean exists = userService.checkDuplicateEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // 🔹 회원 정보 조회 API (이메일 기준)
    @GetMapping("/{email}")
    @ResponseBody
    public ResponseEntity<UserDto> findUser(@PathVariable String email) {
        UserDto userDto = userService.findByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}