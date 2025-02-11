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

    /** 🚀 배송지 목록 조회 */
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

    /** 🚀 배송지 등록 페이지 */
    @GetMapping("/create")
    public String createPage() {
        return "deliveryaddressinfo/create";
    }

    /** 🚀 배송지 등록 */
    @PostMapping
    public String save(@ModelAttribute DeliveryAddressInfoDto dto) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        deliveryAddressInfoService.save(email, dto);
        return "redirect:/users/deliveryaddressinfo";
    }

    /** 🚀 배송지 수정 페이지 */
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

    /** 🚀 배송지 수정 */
    @PostMapping("/update")
    public String update(@ModelAttribute DeliveryAddressInfoDto dto) {
        String email = getAuthenticatedUserEmail();
        if (email == null) {
            return "redirect:/users/login";
        }

        deliveryAddressInfoService.updateByEmailAndAddressName(email, dto);
        return "redirect:/users/deliveryaddressinfo";
    }

    /** 🚀 현재 로그인한 사용자 이메일 가져오기 */
    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return authentication.getName(); // 현재 로그인한 사용자의 이메일 반환
    }
}
