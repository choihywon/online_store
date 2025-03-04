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


    @GetMapping
    public String getAllDeliveries(Model model) {
        List<Delivery> deliveries = adminDeliveryService.getAllDeliveries();
        model.addAttribute("deliveries", deliveries);
        return "admin/deliveries/deliveryList";
    }

    @PostMapping("/{orderId}/shipping")
    public String updateOrderToShipping(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            adminDeliveryService.updateOrderStatusToShipping(orderId);
            redirectAttributes.addFlashAttribute("message", "주문이 배송 중(SHIPPING) 상태로 변경되었습니다.");
            return "redirect:/admin/deliveries";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/orders";
        }
    }

    @GetMapping("/order/{orderId}")
    public String getDeliveriesByOrder(@PathVariable Long orderId, Model model) {
        List<Delivery> deliveries = adminDeliveryService.getDeliveriesByOrder(orderId);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("orderId", orderId);
        return "admin/deliveries/deliveryByOrder";
    }


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
