package com.example.bookstore.order.service;

import com.example.bookstore.order.domain.Order;
import com.example.bookstore.order.domain.OrderItem;
import com.example.bookstore.order.domain.OrderStatus;
import com.example.bookstore.order.dto.OrderDto;
import com.example.bookstore.order.repository.OrderRepository;
import com.example.bookstore.order.repository.OrderItemRepository;
import com.example.bookstore.cart.domain.Cart;
import com.example.bookstore.cart.repository.CartRepository;
import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.inventory.repository.InventoryRepository;
import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.deliveryaddress.repository.DeliveryAddressInfoRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final DeliveryAddressInfoRepository deliveryAddressInfoRepository;

    @Transactional
    public void saveSelectedItems(Long userId, List<Long> cartIds, DeliveryAddressInfo deliveryAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<Cart> selectedCarts = cartRepository.findAllById(cartIds);
        if (selectedCarts.isEmpty()) {
            throw new IllegalStateException("선택된 장바구니 상품이 없습니다.");
        }

        // ✅ 새로운 배송지 입력 시 먼저 저장
        if (deliveryAddress.getId() == null) {
            deliveryAddressInfoRepository.save(deliveryAddress);
        }

        // ✅ 주문 생성
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .deliveryAddress(deliveryAddress)
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        // ✅ 주문 아이템 추가
        for (Cart cart : selectedCarts) {
            Inventory inventory = cart.getInventory();

            // ✅ 주문이 성공하면 재고 차감
            if (cart.getQuantity() > inventory.getQuantity()) {
                throw new IllegalStateException("재고 부족: " + inventory.getTitle() + " (남은 수량: " + inventory.getQuantity() + ")");
            }
            inventory.updateQuantity(inventory.getQuantity() - cart.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .inventory(inventory)
                    .quantity(cart.getQuantity())
                    .price(cart.getInventory().getSalePrice() * cart.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            orderItemRepository.save(orderItem);
        }

        // ✅ 주문한 상품 장바구니에서 삭제
        cartRepository.deleteAll(selectedCarts);
    }

    @Transactional(readOnly = true)
    public DeliveryAddressInfo getDeliveryAddressById(Long deliveryAddressId) {
        return deliveryAddressInfoRepository.findById(deliveryAddressId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 배송지가 존재하지 않습니다."));
    }
    @Transactional(readOnly = true)
    public List<DeliveryAddressInfo> findDeliveryAddressesByUser(User user) {
        return deliveryAddressInfoRepository.findByUser(user);
    }


    @Transactional(readOnly = true)
    public List<OrderDto> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return orderRepository.findByUser(user).stream()
                .map(order -> new OrderDto(order.getOrderId(), order.getStatus(), order.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getUserSeq().equals(userId)) {
            throw new IllegalStateException("자신의 주문만 취소할 수 있습니다.");
        }

        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
        }

        order.updateStatus(OrderStatus.CANCELLED);
    }
}
