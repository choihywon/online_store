package com.example.bookstore.order.controller;

import com.example.bookstore.cart.dto.CartDto;
import com.example.bookstore.cart.repository.CartRepository;
import com.example.bookstore.cart.service.CartService;
import com.example.bookstore.order.dto.OrderDto;
import com.example.bookstore.order.dto.OrderItemDto;
import com.example.bookstore.order.service.OrderItemService;
import com.example.bookstore.order.service.OrderService;
import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @GetMapping
    public String showOrders(Model model) {
        User user = userService.getAuthenticatedUser();
        List<OrderDto> orders = orderService.findByUser(user.getUserSeq());
        model.addAttribute("orders", orders);
        return "users/orders/order";
    }

    @GetMapping("/detail")
    public String orderDetail(@RequestParam Long orderId, Model model) {
        OrderDto order = orderService.findOrderById(orderId);
        List<OrderItemDto> orderItems = orderItemService.findByOrder(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);

        return "users/orders/orderDetail";
    }



@GetMapping("/form")
public String showOrderForm(@RequestParam(required = false) List<Long> cartIds, Model model) {
    User user = userService.getAuthenticatedUser();
    List<DeliveryAddressInfo> deliveryAddresses = orderService.findDeliveryAddressesByUser(user);

    List<CartDto> cartList = null;

    if (cartIds != null && !cartIds.isEmpty()) {
        if (cartRepository.existsById(cartIds.get(0))) {
            cartList = cartService.findCartsByIds(cartIds);
        } else {
            cartList = cartService.findBooksByInventoryIds(cartIds);
        }
    }

    model.addAttribute("deliveryAddresses", deliveryAddresses);
    model.addAttribute("cartList", cartList);
    model.addAttribute("cartIds", cartIds);

    return "users/orders/orderForm";
}



    @PostMapping("/form")
    public String createOrder(
            @RequestParam List<Long> cartIds,
            @RequestParam List<Integer> quantities,
            @RequestParam(required = false) String deliveryAddressId,
            @RequestParam(required = false) String addressName,
            @RequestParam(required = false) String zipcode,
            @RequestParam(required = false) String streetAddr,
            @RequestParam(required = false) String detailAddr,
            @RequestParam(required = false) String etc,
            RedirectAttributes redirectAttributes) {

        if (cartIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "주문할 상품을 선택해야 합니다.");
            return "redirect:/users/orders/form";
        }

        User user = userService.getAuthenticatedUser();
        DeliveryAddressInfo deliveryAddress;

        if (deliveryAddressId == null || deliveryAddressId.trim().isEmpty() || "new".equals(deliveryAddressId)) {
            if (addressName == null || zipcode == null || streetAddr == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "배송지 정보를 입력해주세요.");
                redirectAttributes.addFlashAttribute("cartIds", cartIds);
                return "redirect:/users/orders/form";
            }
            deliveryAddress = new DeliveryAddressInfo(user, addressName, zipcode, streetAddr, detailAddr, etc);
        } else {
            deliveryAddress = orderService.getDeliveryAddressById(Long.parseLong(deliveryAddressId));
        }

        try {
            orderService.saveSelectedItems(user.getUserSeq(), cartIds, quantities, deliveryAddress);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("cartIds", cartIds);
            return "redirect:/users/orders/form";
        }

        return "redirect:/users/orders";
    }








    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam Long orderId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getAuthenticatedUser();
            orderService.cancelOrder(orderId, user.getUserSeq());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users/orders";
    }

}
