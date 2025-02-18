package com.example.bookstore.inventory.domain;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String contents;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = true)
    private LocalDateTime datetime;

    @Column(nullable = true)
    private String authors;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = true)
    private String translators;

    @Column(nullable = false)
    private int price;

    @Column(nullable = true)
    private Integer salePrice;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String lastModifiedBy;

    @Column(nullable = true)
    private String url;

    @Builder
    public Inventory(String title, String contents, String url, String isbn, LocalDateTime datetime,
                     String authors, String publisher, String translators, int price, int salePrice,
                     String thumbnail, int quantity, InventoryStatus status,
                     LocalDateTime createdAt, LocalDateTime lastModifiedAt, String createdBy, String lastModifiedBy) {
        this.title = title;
        this.contents = contents;
        this.url = url;
        this.isbn = isbn;
        this.datetime = datetime;
        this.authors = authors;
        this.publisher = publisher;
        this.translators = translators;
        this.price = price;
        this.salePrice = salePrice;
        this.thumbnail = thumbnail;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
        this.lastModifiedAt = (lastModifiedAt != null) ? lastModifiedAt : LocalDateTime.now();
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }


    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        this.lastModifiedAt = LocalDateTime.now();
    }


    public void updateStatus(InventoryStatus status) {
        this.status = status;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
