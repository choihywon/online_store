package com.example.bookstore.deliveryaddress.repository;
import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAddressInfoRepository extends JpaRepository<DeliveryAddressInfo, Long>{
    List<DeliveryAddressInfo> findByUser(User user); // 회원 ID로 배송지 조회
    boolean existsByUserAndAddressName(User user, String addressName); // 특정 유저가 동일한 배송지 별칭을 가지고 있는지 확인
}
