package com.example.bookstore.cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCartDto {
    private Long userId;       // 사용자 ID
    private Long inventoryId;  // 책 ID
    private int quantity;      // 수량
}
