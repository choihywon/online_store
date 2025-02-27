package com.example.bookstore.order.domain;

public enum OrderStatus {
    PENDING, // 결제대기
    PAID, // 결제완료
    PREPARING, // 배송준비
    SHIPPING, // 배송중
    DELIVERED,
    CANCELLED
}
