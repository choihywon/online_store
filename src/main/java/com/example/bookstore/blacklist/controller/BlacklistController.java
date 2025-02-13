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


    @GetMapping
    public String showBlacklistList(Model model) {

        model.addAttribute("blacklists", blacklistService.findAll());
        return "blacklist/blacklist_list";
    }


    @GetMapping("/add")
    public String showAddBlacklistForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("userEmail", email);
        return "blacklist/add_blacklist";
    }


    @PostMapping
    public String addBlacklist(@RequestParam("email") String email, @RequestParam("reason") String reason) {

        AddBlacklistDto addBlacklistDto = new AddBlacklistDto(email, reason);


        blacklistService.save(addBlacklistDto);
        return "redirect:/admin/blacklist";
    }

    @PostMapping("/delete")
    public String deleteBlacklist(@RequestParam("email") String email, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/users/login";
        }

        blacklistService.unblacklistUser(email);
        return "redirect:/admin/blacklist";
    }
}
