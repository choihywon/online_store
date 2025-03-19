package com.example.bookstore.deliveryaddress.repository;

import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeliveryAddressInfoRepository extends JpaRepository<DeliveryAddressInfo, Long> {

    List<DeliveryAddressInfo> findByUser(User user);
    Optional<DeliveryAddressInfo> findByUserAndAddressName(User user, String addressName);

}
