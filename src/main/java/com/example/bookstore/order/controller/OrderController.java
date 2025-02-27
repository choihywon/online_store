package com.example.bookstore.order.controller;

import com.example.bookstore.order.dto.OrderDto;
import com.example.bookstore.order.dto.OrderItemDto;
import com.example.bookstore.order.service.OrderItemService;
import com.example.bookstore.order.service.OrderService;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;


    @GetMapping
    public String findOrders(Model model) {
        User user = userService.getAuthenticatedUser();
        List<OrderDto> orders = orderService.findByUser(user.getUserSeq());
        model.addAttribute("orders", orders);
        return "users/orders/order";
    }


    @PostMapping
    public String createOrder() {
        User user = userService.getAuthenticatedUser();
        orderService.save(user.getUserSeq());
        return "redirect:/users/orders";
    }


    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam Long orderId) {
        User user = userService.getAuthenticatedUser();
        orderService.cancelOrder(orderId, user.getUserSeq());
        return "redirect:/users/orders";
    }


    @GetMapping("/detail")
    public String orderDetail(@RequestParam Long orderId, Model model) {
        List<OrderItemDto> orderItems = orderItemService.findByOrder(orderId);
        model.addAttribute("orderItems", orderItems);
        return "users/orders/orderDetail";
    }
}
