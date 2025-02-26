package com.example.bookstore.cart.dto;

import com.example.bookstore.inventory.dto.InventoryForUserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Long cartId;
    private InventoryForUserDto inventoryForUserDto;
    private int quantity;
}
