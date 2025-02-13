package com.example.bookstore.blacklist.controller;

import com.example.bookstore.blacklist.dto.AddBlacklistDto;
import com.example.bookstore.blacklist.dto.BlacklistDto;
import com.example.bookstore.blacklist.service.BlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService blacklistService;

    /** ✅ 블랙리스트 목록 페이지 */
    @GetMapping
    public String getBlacklist(Model model) {
        List<BlacklistDto> blacklists = blacklistService.findAll(); // 모든 블랙리스트 조회
        model.addAttribute("blacklists", blacklists);
        return "blacklist/blacklist_list"; // ✅ 목록 페이지로 반환
    }

    /** ✅ 블랙리스트 추가 페이지 */
    @GetMapping("/add")
    public String showAddBlacklistForm() {
        return "blacklist/add_blacklist"; // ✅ 추가 페이지 반환
    }
}
