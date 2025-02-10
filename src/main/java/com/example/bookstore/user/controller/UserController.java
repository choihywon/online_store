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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/mypage/info")
    @ResponseBody
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        System.out.println("✅ authentication.getName(): " + authentication.getName()); // ✅ 로그 추가
        String email = authentication.getName();

        UserDto userDto = userService.findByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    //회원 정보 수정 (닉네임 & 전화번호 변경)
    @PutMapping("/mypage/update")
    @ResponseBody
    public ResponseEntity<String> updateUser(Authentication authentication,
                                             @RequestBody UpdateUserDto updateUserDto) {
        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName(); // ✅ 로그인한 사용자의 이메일 가져오기
        userService.updateUser(email, updateUserDto);
        return new ResponseEntity<>("회원 정보가 수정되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public String joinUser(@ModelAttribute JoinUserDto joinUserDto) {
        userService.joinUser(joinUserDto);
        return "redirect:/users/login";
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginUserDto loginUserDto) {
        UserDto userDto = userService.loginUser(loginUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }



    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean exists = userService.checkDuplicateEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }


    @GetMapping("/{email}")
    @ResponseBody
    public ResponseEntity<UserDto> findUser(@PathVariable String email) {
        UserDto userDto = userService.findByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}