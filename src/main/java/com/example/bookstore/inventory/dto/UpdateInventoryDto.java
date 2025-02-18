package com.example.bookstore.inventory.dto;

import com.example.bookstore.inventory.domain.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryDto {
    private Long inventoryId;
    private int quantity;
    private InventoryStatus status;
}
