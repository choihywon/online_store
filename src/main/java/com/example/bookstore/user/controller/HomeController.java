package com.example.bookstore.user.controller;

import com.example.bookstore.inventory.dto.InventoryForUserDto;
import com.example.bookstore.inventory.service.UserInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserInventoryService userInventoryService;

    @GetMapping("/")
    public String index(Model model) {
        List<InventoryForUserDto> books = userInventoryService.findAllOnSaleBooks();  // ✅ ON_SALES 책만 조회
        model.addAttribute("books", books);
        return "index";
    }
}
