package com.example.bookstore.user.controller;

import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/detail")
    public ResponseEntity<UserDto> getUserDetail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
    //회원 정보 조회
    @GetMapping("/user/edit")
    public ResponseEntity<UserDto> getUserEdit(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //회원 정보 수정
    @PostMapping("/user/edit")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        userService.update(userDto);
        return ResponseEntity.ok(userDto);
    }

    //회원 탈퇴
    //진짜 지워도 되나? 비활성화 생각하기
    @PostMapping("/user/delete")
    public ResponseEntity<UserDto> deleteUser(@RequestParam String email) {
        userService.delete(userId);
        return ResponseEntity.ok("redirect:/");
    }

}
