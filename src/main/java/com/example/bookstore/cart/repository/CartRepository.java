package com.example.bookstore.cart.repository;

import com.example.bookstore.cart.domain.Cart;
import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 특정 유저의 장바구니 목록 조회
    List<Cart> findByUser(User user);

    // 특정 장바구니 아이템 삭제
    void deleteById(Long cartId);

    // 특정 유저가 이미 해당 책을 장바구니에 담았는지 확인
    boolean existsByUserAndInventory(User user, Inventory inventory);

    // 특정 유저의 특정 장바구니 아이템 조회
    Optional<Cart> findByUserAndInventory(User user, Inventory inventory);
}
