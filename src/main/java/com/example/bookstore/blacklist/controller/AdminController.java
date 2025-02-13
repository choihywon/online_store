package com.example.bookstore.blacklist.controller;

import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller  // ✅ @RestController → @Controller 변경
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    /** ✅ 유저 관리 페이지 */
    @GetMapping
    public String getAllUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user_list"; // ✅ user_list.html 반환
    }

    @GetMapping("/{email}")
    public String getUserDetail(@PathVariable String email, Model model) {
        UserDto user = userService.findUserByEmail(email); // ✅ 올바른 메서드 호출
        model.addAttribute("user", user);
        return "admin/user_detail";
    }
}
