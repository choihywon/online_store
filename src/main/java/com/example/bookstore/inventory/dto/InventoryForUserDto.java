package com.example.bookstore.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryForUserDto {
    private Long inventoryId;
    private String title;
    private String isbn;
    private String[] authors;
    private String publisher;
    private int salePrice;
    private String thumbnail;
}