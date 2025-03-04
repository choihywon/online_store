package com.example.bookstore.delivery.service;

import com.example.bookstore.delivery.domain.Delivery;
import com.example.bookstore.delivery.domain.DeliveryStatus;
import com.example.bookstore.delivery.repository.DeliveryRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.repository.UserRepository;
import com.example.bookstore.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserDeliveryService {

    private final DeliveryRepository deliveryRepository;


    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByUser(Long userSeq) {
        return deliveryRepository.findAll().stream()
                .filter(delivery -> delivery.getOrder() != null && delivery.getOrder().getUser().getUserSeq().equals(userSeq))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Delivery getDeliveryById(UUID deliveryId, Long userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송이 존재하지 않습니다."));


        if (delivery.getOrder() == null) {
            throw new IllegalStateException("배송에 연결된 주문이 없습니다.");
        }


        if (!delivery.getOrder().getUser().getUserSeq().equals(userId)) {
            throw new SecurityException("본인의 배송만 조회할 수 있습니다.");
        }

        return delivery;
    }
}
