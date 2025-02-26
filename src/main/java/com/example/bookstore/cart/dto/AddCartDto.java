package com.example.bookstore.cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCartDto {
    private Long userId;
    private Long inventoryId;
    private int quantity;
}
