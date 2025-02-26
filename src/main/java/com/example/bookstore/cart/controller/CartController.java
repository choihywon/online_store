package com.example.bookstore.cart.controller;

import com.example.bookstore.cart.dto.AddCartDto;
import com.example.bookstore.cart.dto.CartDto;
import com.example.bookstore.cart.dto.UpdateCartDto;
import com.example.bookstore.cart.service.CartService;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String findAll(Model model) {
        User user = userService.getAuthenticatedUser();
        List<CartDto> cartList = cartService.findAll(user);
        model.addAttribute("cartList", cartList);
        return "users/carts/cart"; // ✅ 여기 확인
    }


    /** 📌 장바구니 상품 추가 */
    @PostMapping("/add")
    public String addCart(@RequestParam Long inventoryId, @RequestParam int quantity) {
        User user = userService.getAuthenticatedUser();
        AddCartDto dto = new AddCartDto(user.getUserSeq(), inventoryId, quantity);
        cartService.save(dto);
        return "redirect:/users/carts"; // ✅ 장바구니 페이지로 이동
    }

    /** 📌 장바구니 상품 수량 수정 */
    @PostMapping("/edit")
    public String updateCart(@ModelAttribute UpdateCartDto dto) {
        cartService.updateById(dto);
        return "redirect:/users/carts";
    }

    /** 📌 장바구니 상품 삭제 */
    @PostMapping("/delete")
    public String deleteFromCart(@RequestParam Long cartId) {
        cartService.deleteById(cartId);
        return "redirect:/users/carts";
    }
}
