package com.example.bookstore.delivery.repository;

import com.example.bookstore.delivery.domain.Delivery;
import com.example.bookstore.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    List<Delivery> findByDeliveryAddressInfoId(Long deliveryAddressId);
    List<Delivery> findByOrder(Order order);
}
