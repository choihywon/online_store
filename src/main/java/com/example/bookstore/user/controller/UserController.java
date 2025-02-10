package com.example.bookstore.user.controller;

import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.dto.LoginUserDto;
import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입 API
    @PostMapping("/join")
    public String register(@RequestBody JoinUserDto joinUserDto) {
        userService.save(joinUserDto);
        return "회원가입이 완료되었습니다!";
    }

    //로그인 API (Spring Security 처리)
    @PostMapping("/login")
    public String login(@RequestBody LoginUserDto loginUserDto) {
        return "로그인 성공!";
    }

    //마이페이지 조회 API
    @GetMapping
    public Optional<UserDto> myPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.findByEmail(email);
    }

//    //회원 정보 수정 API
//    @PutMapping
//    public String updateUser(@RequestBody UpdateUserDto updateUserDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        Optional<UserDto> userDto = userService.findByEmail(email);
//
//        if (userDto.isPresent()) {
//            userService.update(userDto.get().getEmail(), updateUserDto);
//            return "회원 정보 수정이 완료되었습니다!";
//        } else {
//            return "사용자를 찾을 수 없습니다.";
//        }
//    }
//
//    // 회원 탈퇴 API
//    @DeleteMapping
//    public String deleteUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        Optional<UserDto> userDto = userService.findByEmail(email);
//
//        if (userDto.isPresent()) {
//            userService.delete(userDto.get().getEmail());
//            return "회원 탈퇴가 완료되었습니다.";
//        } else {
//            return "사용자를 찾을 수 없습니다.";
//        }
//    }
}