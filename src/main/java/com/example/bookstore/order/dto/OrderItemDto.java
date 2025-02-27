package com.example.bookstore.order.dto;

import com.example.bookstore.inventory.dto.InventoryForUserDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long orderItemId;
    private InventoryForUserDto inventoryForUserDto;
    private int quantity;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}