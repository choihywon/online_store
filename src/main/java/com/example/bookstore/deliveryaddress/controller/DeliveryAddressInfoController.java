package com.example.bookstore.deliveryaddress.controller;

import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.service.DeliveryAddressInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/deliveryaddressinfo")
@RequiredArgsConstructor
public class DeliveryAddressInfoController {

    private final DeliveryAddressInfoService deliveryAddressInfoService;

    @GetMapping
    @ResponseBody
    public List<DeliveryAddressInfoDto> list(Authentication authentication) {
        String email = authentication.getName();
        return deliveryAddressInfoService.findByUser(email);
    }

    @GetMapping("/create")
    public String createPage() {
        return "deliveryaddressinfo/create";
    }

    @PostMapping
    public String save(Authentication authentication, @ModelAttribute DeliveryAddressInfoDto dto) {
        String email = authentication.getName();
        deliveryAddressInfoService.save(email, dto);
        return "redirect:/users/deliveryaddressinfo";
    }

    @GetMapping("/edit")
    public String editPage() {
        return "deliveryaddressinfo/edit";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        deliveryAddressInfoService.delete(id);
        return "redirect:/users/deliveryaddressinfo";
    }
}
