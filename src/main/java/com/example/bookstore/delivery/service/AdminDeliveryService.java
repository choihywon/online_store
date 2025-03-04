package com.example.bookstore.delivery.service;

import com.example.bookstore.delivery.domain.Delivery;
import com.example.bookstore.delivery.domain.DeliveryStatus;
import com.example.bookstore.delivery.repository.DeliveryRepository;
import com.example.bookstore.order.domain.Order;
import com.example.bookstore.order.domain.OrderStatus;
import com.example.bookstore.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;


    @Transactional(readOnly = true)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        return deliveryRepository.findByOrder(order);
    }


    @Transactional
    public void updateDeliveryStatus(UUID deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송 정보를 찾을 수 없습니다."));


        delivery.setStatus(status);
        if (status == DeliveryStatus.DELIVERED) {
            delivery.setArrivedAt(LocalDateTime.now());
        }
        deliveryRepository.save(delivery);
    }


    @Transactional
    public void updateOrderStatusToShipping(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));


        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송이 진행 중이거나 완료된 주문입니다.");
        }


        order.updateStatus(OrderStatus.SHIPPING);
        orderRepository.save(order);


        Delivery delivery = Delivery.builder()
                .id(UUID.randomUUID())
                .order(order)
                .deliveryAddressInfo(order.getDeliveryAddress())
                .status(DeliveryStatus.SHIPPING)
                .startedAt(LocalDateTime.now())
                .build();

        deliveryRepository.save(delivery);
    }

}
