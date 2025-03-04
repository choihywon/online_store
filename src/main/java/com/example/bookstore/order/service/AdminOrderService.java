package com.example.bookstore.order.service;

import com.example.bookstore.delivery.domain.Delivery;
import com.example.bookstore.delivery.domain.DeliveryStatus;
import com.example.bookstore.delivery.repository.DeliveryRepository;
import com.example.bookstore.delivery.service.AdminDeliveryService;
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
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final AdminDeliveryService adminDeliveryService;
    private final DeliveryRepository deliveryRepository;

    // 📌 모든 주문 목록 조회 (관리자용)
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 📌 일반적인 주문 상태 변경 (배송 생성 X)
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        order.updateStatus(status);
        orderRepository.save(order);
    }
    @Transactional
    public void updateOrderStatusToShipping(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        System.out.println("🚀 주문 상태 변경 시도: " + orderId + " 현재 상태: " + order.getStatus());

        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송이 진행 중이거나 완료된 주문입니다.");
        }

        order.updateStatus(OrderStatus.SHIPPING);
        orderRepository.save(order);

        System.out.println("✅ 주문 상태 변경 완료: " + orderId + " → SHIPPING");

        Delivery delivery = Delivery.builder()
                .id(UUID.randomUUID()) // ✅ UUID 자동 생성
                .order(order)
                .deliveryAddressInfo(order.getDeliveryAddress())
                .status(DeliveryStatus.SHIPPING)
                .startedAt(LocalDateTime.now())
                .build();

        deliveryRepository.save(delivery);
        System.out.println("✅ 배송 생성 완료: " + delivery.getId());
    }


}
