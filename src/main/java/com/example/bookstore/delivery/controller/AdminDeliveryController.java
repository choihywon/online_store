package com.example.bookstore.delivery.controller;

import com.example.bookstore.delivery.service.AdminDeliveryService;
import com.example.bookstore.delivery.domain.Delivery;
import com.example.bookstore.delivery.domain.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/deliveries")
@RequiredArgsConstructor
public class AdminDeliveryController {

    private final AdminDeliveryService adminDeliveryService;

    // 📌 관리자: 모든 배송 목록 조회
    @GetMapping
    public String getAllDeliveries(Model model) {
        List<Delivery> deliveries = adminDeliveryService.getAllDeliveries();
        model.addAttribute("deliveries", deliveries);
        return "admin/deliveries/deliveryList";
    }
    // 📌 관리자: 주문 상태를 SHIPPING으로 변경
    @PostMapping("/{orderId}/shipping")
    public String updateOrderToShipping(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            adminDeliveryService.updateOrderStatusToShipping(orderId);
            redirectAttributes.addFlashAttribute("message", "주문이 배송 중(SHIPPING) 상태로 변경되었습니다.");
            return "redirect:/admin/deliveries"; // ✅ 배송 목록 페이지로 이동
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/orders"; // ❌ 에러 발생 시 다시 주문 목록으로 이동
        }
    }
    // 📌 특정 주문(Order)과 관련된 배송 조회
    @GetMapping("/order/{orderId}")
    public String getDeliveriesByOrder(@PathVariable Long orderId, Model model) {
        List<Delivery> deliveries = adminDeliveryService.getDeliveriesByOrder(orderId);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("orderId", orderId);
        return "admin/deliveries/deliveryByOrder";
    }

    // 📌 관리자: 배송 상태 변경 (SHIPPING → DELIVERED)
    @PostMapping("/{deliveryId}/status")
    public String updateDeliveryStatus(@PathVariable UUID deliveryId,
                                       @RequestParam DeliveryStatus status,
                                       RedirectAttributes redirectAttributes) {
        try {
            adminDeliveryService.updateDeliveryStatus(deliveryId, status);
            redirectAttributes.addFlashAttribute("message", "배송 상태가 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/deliveries";
    }
}
