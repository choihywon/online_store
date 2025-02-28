package com.example.bookstore.order.domain;

import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    // ✅ 배송지 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deliveries_info_seq", nullable = false)
    private DeliveryAddressInfo deliveryAddress;

    @Builder
    public Order(User user, OrderStatus status, DeliveryAddressInfo deliveryAddress) {
        this.user = user;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
