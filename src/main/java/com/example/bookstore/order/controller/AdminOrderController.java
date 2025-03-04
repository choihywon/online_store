package com.example.bookstore.order.controller;

import com.example.bookstore.delivery.service.AdminDeliveryService;
import com.example.bookstore.order.service.AdminOrderService;
import com.example.bookstore.order.domain.Order;
import com.example.bookstore.order.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final AdminDeliveryService adminDeliveryService;

    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = adminOrderService.getAllOrders();

        if (orders == null || orders.isEmpty()) {
            throw new IllegalStateException("주문 목록이 없습니다.");
        }

        model.addAttribute("orders", orders);
        return "admin/orders/orderList";
    }


    @PostMapping("/{orderId}/shipping")
    public String updateOrderToShipping(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            adminDeliveryService.updateOrderStatusToShipping(orderId);
            redirectAttributes.addFlashAttribute("message", "주문이 배송 중(SHIPPING) 상태로 변경되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/orders";
    }


    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId,
                                    @RequestParam OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        try {
            adminOrderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("message", "주문 상태가 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "주문을 찾을 수 없습니다. (ID: " + orderId + ")");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
