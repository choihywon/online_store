package com.example.bookstore.user.controller;

import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //마이페이지 조회 (유저 상세 정보)
    @GetMapping("/user/detail")
    public ResponseEntity<UserDto> getUserDetail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //회원 정보 수정 페이지 (현재 정보 불러오기)
    @GetMapping("/user/edit")
    public ResponseEntity<UserDto> getUserEdit(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //회원 정보 수정 (수정 후 `/users`로 리다이렉트)
    @PostMapping
    public ResponseEntity<String> updateUser(
            @RequestParam Long userId,
            @RequestBody UpdateUserDto updateUserDto) {
        userService.update(userId, updateUserDto);
        return ResponseEntity.ok("redirect:/users");
    }

    // 회원 탈퇴 (탈퇴 후 `/`로 리다이렉트)
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok("redirect:/");
    }
}