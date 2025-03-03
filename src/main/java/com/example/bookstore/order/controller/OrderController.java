package com.example.bookstore.order.controller;

import com.example.bookstore.cart.dto.CartDto;
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

    @GetMapping
    public String showOrders(Model model) {
        User user = userService.getAuthenticatedUser();
        List<OrderDto> orders = orderService.findByUser(user.getUserSeq());
        model.addAttribute("orders", orders);
        return "users/orders/order";
    }

    @GetMapping("/detail")
    public String orderDetail(@RequestParam Long orderId, Model model) {
        List<OrderItemDto> orderItems = orderItemService.findByOrder(orderId);
        model.addAttribute("orderItems", orderItems);
        return "users/orders/orderDetail";
    }

    @GetMapping("/form")
    public String showOrderForm(Model model) {
        User user = userService.getAuthenticatedUser();
        List<DeliveryAddressInfo> deliveryAddresses = orderService.findDeliveryAddressesByUser(user);
        List<CartDto> cartList = cartService.findAll(user);

        model.addAttribute("deliveryAddresses", deliveryAddresses);
        model.addAttribute("cartList", cartList);

        return "users/orders/orderForm"; // ✅ GET 요청은 주문 폼을 보여줌
    }


//    @PostMapping("/form")
//    public String createOrder(
//            @RequestParam(required = false) List<Long> cartIds,
//            @RequestParam(required = false) Long deliveryAddressId,
//            @RequestParam(required = false) String addressName,
//            @RequestParam(required = false) String zipcode,
//            @RequestParam(required = false) String streetAddr,
//            @RequestParam(required = false) String detailAddr,
//            @RequestParam(required = false) String etc,
//            RedirectAttributes redirectAttributes) {
//
//        if (cartIds == null || cartIds.isEmpty()) {
//            redirectAttributes.addFlashAttribute("errorMessage", "주문할 상품을 선택해야 합니다.");
//            return "redirect:/users/orders/form"; // ✅ 주문 폼으로 리디렉션
//        }
//
//        User user = userService.getAuthenticatedUser();
//        DeliveryAddressInfo deliveryAddress = (deliveryAddressId != null) ?
//                orderService.getDeliveryAddressById(deliveryAddressId) :
//                new DeliveryAddressInfo(user, addressName, zipcode, streetAddr, detailAddr, etc);
//
//        orderService.saveSelectedItems(user.getUserSeq(), cartIds, deliveryAddress);
//        return "redirect:/users/orders";
//    }

    @PostMapping("/form")
    public String createOrder(
            @RequestParam(required = false) List<Long> cartIds,
            @RequestParam(required = false) String deliveryAddressId, // ✅ 기존 배송지 ID
            @RequestParam(required = false) String addressName, // ✅ 새 배송지 정보
            @RequestParam(required = false) String zipcode,
            @RequestParam(required = false) String streetAddr,
            @RequestParam(required = false) String detailAddr,
            @RequestParam(required = false) String etc,
            RedirectAttributes redirectAttributes) {

        if (cartIds == null || cartIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "주문할 상품을 선택해야 합니다.");
            return "redirect:/users/orders/form"; // ✅ 주문 폼으로 다시 이동
        }

        User user = userService.getAuthenticatedUser();
        DeliveryAddressInfo deliveryAddress;

        if ("new".equals(deliveryAddressId)) {
            // ✅ 새로운 배송지를 생성하여 저장
            deliveryAddress = new DeliveryAddressInfo(user, addressName, zipcode, streetAddr, detailAddr, etc);
        } else {
            // ✅ 기존 배송지 사용
            deliveryAddress = orderService.getDeliveryAddressById(Long.parseLong(deliveryAddressId));
        }

        orderService.saveSelectedItems(user.getUserSeq(), cartIds, deliveryAddress);
        return "redirect:/users/orders";
    }



    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam Long orderId) {
        User user = userService.getAuthenticatedUser();
        orderService.cancelOrder(orderId, user.getUserSeq());
        return "redirect:/users/orders";
    }
}
