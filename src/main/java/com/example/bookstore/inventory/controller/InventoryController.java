package com.example.bookstore.inventory.controller;

import com.example.bookstore.inventory.dto.KakaoBookResponseDto;
import com.example.bookstore.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory/books")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/search-page")
    public String searchPage() {
        return "book-search"; // 📌 book-search.html 반환
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<KakaoBookResponseDto> searchBooks(@RequestParam String query) {
        KakaoBookResponseDto response = inventoryService.searchBooks(query);
        return ResponseEntity.ok(response);
    }
}
