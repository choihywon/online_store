package com.example.bookstore.blacklist.controller;

import com.example.bookstore.blacklist.dto.AddBlacklistDto;
import com.example.bookstore.blacklist.service.BlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService blacklistService;

    // 블랙리스트 목록 페이지 (GET 방식)
    @GetMapping
    public String showBlacklistList(Model model) {
        // 블랙리스트 목록을 가져와서 모델에 추가
        model.addAttribute("blacklists", blacklistService.findAll());
        return "blacklist/blacklist_list";  // blacklist_list.html로 매핑
    }

    // 블랙리스트 등록 페이지 (GET 방식)
    @GetMapping("/add")
    public String showAddBlacklistForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("userEmail", email);  // 이메일을 모델에 추가
        return "blacklist/add_blacklist";  // add_blacklist.html로 매핑
    }

    // 블랙리스트 등록 처리 (POST 방식)
    @PostMapping
    public String addBlacklist(@RequestParam("email") String email, @RequestParam("reason") String reason) {
        // AddBlacklistDto 객체 생성
        AddBlacklistDto addBlacklistDto = new AddBlacklistDto(email, reason);

        // 블랙리스트 등록
        blacklistService.save(addBlacklistDto);  // 하나의 객체로 전달
        return "redirect:/admin/blacklist";  // 블랙리스트 목록으로 리다이렉트
    }
}
