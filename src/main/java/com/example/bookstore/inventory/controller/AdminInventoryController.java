package com.example.bookstore.inventory.controller;

import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.inventory.dto.AddInventoryDto;
import com.example.bookstore.inventory.dto.InventoryForAdminDto;
import com.example.bookstore.inventory.dto.UpdateInventoryDto;
import com.example.bookstore.inventory.service.AdminInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {
    private final AdminInventoryService adminInventoryService;


    @GetMapping("/search-page")
    public String searchBookPage() {
        return "admin/book-search";
    }


    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String query,
                              @RequestParam(defaultValue = "1") int page,
                              Model model) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("books", adminInventoryService.searchBooks(query, page).get("books"));
            model.addAttribute("query", query);
            model.addAttribute("page", page);
            model.addAttribute("isEnd", adminInventoryService.searchBooks(query, page).get("isEnd"));
        }
        return "admin/book-search";
    }


    @GetMapping("/detail")
    public String bookDetail(@RequestParam String title,
                             @RequestParam String isbn,
                             @RequestParam String authors,
                             @RequestParam(required = false, defaultValue = "없음") String translators,
                             @RequestParam String publisher,
                             @RequestParam(required = false) String datetime,
                             @RequestParam int price,
                             @RequestParam(required = false, defaultValue = "0") int salePrice,
                             @RequestParam String thumbnail,
                             @RequestParam(required = false, defaultValue = "") String url,
                             @RequestParam String status,
                             @RequestParam(required = false, defaultValue = "책 설명이 없습니다.") String contents,
                             Model model) {
        System.out.println("상세 페이지 - ISBN: " + isbn);
        System.out.println("상세 페이지 -  datetime: " + datetime);


        LocalDateTime parsedDatetime = null;
        if (datetime != null && !datetime.isEmpty()) {
            try {
                parsedDatetime = LocalDateTime.parse(datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } catch (Exception e) {
                System.out.println("datetime 오류: " + e.getMessage());
            }
        }

        model.addAttribute("title", title);
        model.addAttribute("isbn", isbn);
        model.addAttribute("authors", authors);
        model.addAttribute("translators", translators);
        model.addAttribute("publisher", publisher);
        model.addAttribute("datetime", parsedDatetime != null ? parsedDatetime : "2000-01-01T00:00:00");
        model.addAttribute("price", price);
        model.addAttribute("salePrice", salePrice);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("url", url);
        model.addAttribute("status", status);
        model.addAttribute("contents", contents);
        return "admin/book-detail";
    }


    @PostMapping("/add")
    public String addBook(@ModelAttribute AddInventoryDto dto) {
        System.out.println("add isbn" + dto.getIsbn());
        System.out.println("add datetime" + dto.getDatetime());
        adminInventoryService.save(dto);
        return "redirect:/admin/inventory";
    }


    @GetMapping
    public String findAllBooks(Model model) {
        List<InventoryForAdminDto> books = adminInventoryService.findAll();
        model.addAttribute("books", books);
        return "admin/inventory";
    }


    @GetMapping("/editForm")
    public String editBookForm(@RequestParam Long id, Model model) {
        InventoryForAdminDto book = adminInventoryService.findById(id);
        model.addAttribute("book", book);
        return "admin/book-edit";
    }



    @PostMapping("/edit")
    public String update(@ModelAttribute UpdateInventoryDto dto) {
        System.out.println("수정 요청 ID: " + dto.getInventoryId());
        System.out.println("수정할 수량: " + dto.getQuantity());
        System.out.println("수정할 상태: " + dto.getStatus());

        adminInventoryService.update(dto);
        return "redirect:/admin/inventory/" + dto.getInventoryId();
    }



    @GetMapping("/{id}")
    public String findBookById(@PathVariable Long id, Model model) {
        System.out.println("Controller - 전달받은 ID: " + id);

        Inventory book = adminInventoryService.findInventoryByInventoryId(id);
        model.addAttribute("book", book);

        return "admin/inventory-detail";
    }


}
