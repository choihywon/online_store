package com.example.bookstore.order.repository;

import com.example.bookstore.order.domain.Order;
import com.example.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);  //
}
