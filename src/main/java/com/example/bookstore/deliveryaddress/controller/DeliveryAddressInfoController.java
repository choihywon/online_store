package com.example.bookstore.deliveryaddress.controller;

import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.service.DeliveryAddressInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/deliveryaddressinfo")
@RequiredArgsConstructor
public class DeliveryAddressInfoController {

    private final DeliveryAddressInfoService deliveryAddressInfoService;

    @GetMapping
    public String listPage(Model model) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        List<DeliveryAddressInfoDto> deliveries = deliveryAddressInfoService.findByUserEmail(email);
        model.addAttribute("deliveries", deliveries);
        return "deliveryaddressinfo/list";
    }

    @GetMapping("/create")
    public String createPage() {
        return "deliveryaddressinfo/create";
    }

    @PostMapping
    public String save(@ModelAttribute DeliveryAddressInfoDto dto) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        deliveryAddressInfoService.save(email, dto);
        return "redirect:/users/deliveryaddressinfo";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("addressName") String addressName, Model model) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        DeliveryAddressInfoDto delivery = deliveryAddressInfoService.findByUserEmailAndAddressName(email, addressName);
        model.addAttribute("delivery", delivery);
        return "deliveryaddressinfo/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute DeliveryAddressInfoDto dto) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        deliveryAddressInfoService.updateByEmailAndAddressName(email, dto);
        return "redirect:/users/deliveryaddressinfo";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("addressName") String addressName) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        deliveryAddressInfoService.deleteByEmailAndAddressName(email, addressName);
        return "redirect:/users/deliveryaddressinfo";
    }


    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return authentication.getName(); // 현재 로그인한 사용자의 이메일 반환
    }
}
