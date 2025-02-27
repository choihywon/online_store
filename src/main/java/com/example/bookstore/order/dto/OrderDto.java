package com.example.bookstore.order.dto;

import com.example.bookstore.order.domain.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long orderId;
    private OrderStatus status;
    private LocalDateTime createdAt;
}