package com.example.bookstore.order.service;

import com.example.bookstore.order.domain.Order;
import com.example.bookstore.order.dto.OrderItemDto;
import com.example.bookstore.cart.repository.CartRepository;
import com.example.bookstore.order.repository.OrderItemRepository;
import com.example.bookstore.order.repository.OrderRepository;
import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.inventory.dto.InventoryForUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public List<OrderItemDto> findByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        return orderItemRepository.findByOrder(order).stream()
                .map(orderItem -> new OrderItemDto(
                        orderItem.getOrderItemId(),
                        new InventoryForUserDto(
                                orderItem.getInventory().getInventoryId(),
                                orderItem.getInventory().getTitle(),
                                orderItem.getInventory().getIsbn(),
                                orderItem.getInventory().getAuthors() != null ?
                                        orderItem.getInventory().getAuthors().split(",") : new String[]{} ,
                                orderItem.getInventory().getPublisher(),
                                orderItem.getInventory().getSalePrice(),
                                orderItem.getInventory().getThumbnail()
                        ),
                        orderItem.getQuantity(),
                        orderItem.getPrice(),
                        orderItem.getCreatedAt(),
                        orderItem.getLastModifiedAt()
                ))
                .collect(Collectors.toList());
    }

    private InventoryForUserDto convertToDto(Inventory inventory) {
        return new InventoryForUserDto(
                inventory.getInventoryId(),
                inventory.getTitle(),
                inventory.getIsbn(),
                inventory.getAuthors() != null ? inventory.getAuthors().split(",") : new String[]{},
                inventory.getPublisher(),
                inventory.getSalePrice(),
                inventory.getThumbnail()
        );
    }
}