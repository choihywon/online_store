package com.example.bookstore.delivery.controller;

import com.example.bookstore.delivery.domain.Delivery;
import com.example.bookstore.delivery.service.UserDeliveryService;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users/deliveries")
@RequiredArgsConstructor
public class UserDeliveryController {

    private final UserDeliveryService userDeliveryService;
    private final UserService userService;

    // 📌 사용자의 배송 목록 조회
    @GetMapping
    public String showDeliveries(Model model) {
        User user = userService.getAuthenticatedUser();
        List<Delivery> deliveries = userDeliveryService.getDeliveriesByUser(user.getUserSeq());
        model.addAttribute("deliveries", deliveries);
        return "users/deliveries/deliveryList";
    }

    // 📌 배송 상세 조회 (본인의 배송만 가능)
    @GetMapping("/{deliveryId}")
    public String deliveryDetail(@PathVariable UUID deliveryId, Model model) {
        User user = userService.getAuthenticatedUser();
        Delivery delivery = userDeliveryService.getDeliveryById(deliveryId, user.getUserSeq());
        model.addAttribute("delivery", delivery);
        return "users/deliveries/deliveryDetail";
    }
}
