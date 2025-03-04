package com.example.bookstore.delivery.domain;

public enum DeliveryStatus {
    PENDING,     // 배송 준비 중
    SHIPPING,    // 배송 중
    DELIVERED,   // 배송 완료
    CANCELLED    // 배송 취소됨
}
