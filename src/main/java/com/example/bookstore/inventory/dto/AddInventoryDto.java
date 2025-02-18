package com.example.bookstore.inventory.dto;

import com.example.bookstore.inventory.domain.InventoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class AddInventoryDto {
    private String title;

    private String contents;
    private String url;
    private String isbn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datetime;
    private String[] authors;

    private String publisher;
    private String[] translators;
    private int price;
    private int salePrice;
    private String thumbnail;
    private InventoryStatus status;
    private int quantity;
}
