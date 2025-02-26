package com.example.bookstore.cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartDto {
    private Long cartId;   // 장바구니 ID
    private int quantity;  // 변경할 수량
}
