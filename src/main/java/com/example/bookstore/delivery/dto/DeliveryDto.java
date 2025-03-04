package com.example.bookstore.delivery.dto;

import com.example.bookstore.delivery.domain.DeliveryStatus;
import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    private UUID id;
    private DeliveryAddressInfo deliveryAddressInfo;
    private DeliveryStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime arrivedAt;
}
