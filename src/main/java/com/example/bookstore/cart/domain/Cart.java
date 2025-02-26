package com.example.bookstore.cart.domain;

import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryId", nullable = false)
    private Inventory inventory;

    private int quantity;

    private LocalDateTime addedAt;

    private LocalDateTime lastModifiedAt;

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
