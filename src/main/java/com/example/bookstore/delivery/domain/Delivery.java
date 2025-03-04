package com.example.bookstore.delivery.domain;

import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.order.domain.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(generator = "UUID") // ✅ UUID 자동 생성
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    // ✅ Order와 연결 추가 (🚚 배송이 어느 주문에서 생성되었는지 추적)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private DeliveryAddressInfo deliveryAddressInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime arrivedAt;

    // ✅ Order와 연결된 배송 생성
    public static Delivery createDelivery(Order order) {
        return Delivery.builder()
                .id(UUID.randomUUID())
                .order(order) // ✅ 주문과 연결
                .deliveryAddressInfo(order.getDeliveryAddress())
                .status(DeliveryStatus.SHIPPING)
                .startedAt(LocalDateTime.now())
                .build();
    }

    // ✅ 배송 상태 변경 메서드
    public void updateStatus(DeliveryStatus status) {
        this.status = status;
        if (status == DeliveryStatus.DELIVERED) {
            this.arrivedAt = LocalDateTime.now();
        }
    }
}
