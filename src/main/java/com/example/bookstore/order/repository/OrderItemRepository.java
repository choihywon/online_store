package com.example.bookstore.order.repository;



import com.example.bookstore.order.domain.OrderItem;
import com.example.bookstore.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);  //
}

