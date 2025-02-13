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

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user_list";
    }

    @GetMapping("/{email}")
    public String getUserDetail(@PathVariable String email, Model model) {
        UserDto user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "admin/user_detail";
    }
}
