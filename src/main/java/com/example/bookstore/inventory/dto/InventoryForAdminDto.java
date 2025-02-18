package com.example.bookstore.inventory.dto;

import com.example.bookstore.inventory.domain.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class InventoryForAdminDto {
    private Long inventoryId;
    private String title;
    private String isbn;
    private String[] authors;
    private String publisher;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;
    private int price;
    private int salePrice;
    private String thumbnail;
    private int quantity;
    private InventoryStatus status;
    private String[] translators;
}
