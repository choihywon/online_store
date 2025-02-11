package com.example.bookstore.deliveryaddress.repository;

import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAddressInfoRepository extends JpaRepository<DeliveryAddressInfo, Long> {
    List<DeliveryAddressInfo> findByUser(User user);

    boolean existsByUserAndAddressName(User user, String addressName); // ✅ destName → addressName 변경
}
