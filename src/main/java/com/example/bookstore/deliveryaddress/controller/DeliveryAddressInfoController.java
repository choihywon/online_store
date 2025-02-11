package com.example.bookstore.deliveryaddress.controller;

import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.service.DeliveryAddressInfoService;
import lombok.RequiredArgsConstructor;
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
    public String listPage(@RequestParam String email, Model model) {
        List<DeliveryAddressInfoDto> deliveries = deliveryAddressInfoService.findByUser(email);
        model.addAttribute("deliveries", deliveries);
        return "deliveryaddressinfo/list"; // `deliveryaddressinfo/list.html`로 반환
    }

    /** 🚀 배송지 등록 페이지 */
    @GetMapping("/create")
    public String createPage() {
        return "deliveryaddressinfo/create"; // `deliveryaddressinfo/create.html`로 반환
    }

    /** 🚀 배송지 등록 */
    @PostMapping
    public String save(@RequestParam String email, @ModelAttribute DeliveryAddressInfoDto dto) {
        deliveryAddressInfoService.save(email, dto);
        return "redirect:/users/deliveryaddressinfo"; // 배송지 목록 페이지로 리디렉션
    }

    /** 🚀 배송지 수정 페이지 (주소 이름으로 조회) */
    @GetMapping("/edit")
    public String editPage(@RequestParam String addressName, @RequestParam String email, Model model) {
        DeliveryAddressInfoDto delivery = deliveryAddressInfoService.findByUserAndAddressName(email, addressName);
        model.addAttribute("delivery", delivery);
        return "deliveryaddressinfo/edit"; // `deliveryaddressinfo/edit.html`로 반환
    }

    /** 🚀 배송지 수정 */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute DeliveryAddressInfoDto dto) {
        deliveryAddressInfoService.update(id, dto);
        return "redirect:/users/deliveryaddressinfo"; // 배송지 목록 페이지로 리디렉션
    }

    /** 🚀 배송지 삭제 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        deliveryAddressInfoService.delete(id);
        return "redirect:/users/deliveryaddressinfo"; // 배송지 목록 페이지로 리디렉션
    }
}
