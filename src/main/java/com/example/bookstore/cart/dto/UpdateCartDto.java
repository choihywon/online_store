package com.example.bookstore.cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartDto {
    private Long cartId;
    private int quantity;
}
