package com.example.bookstore.deliveryaddress.service;

import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.deliveryaddress.repository.DeliveryAddressInfoRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAddressInfoService {

    private final DeliveryAddressInfoRepository deliveryAddressInfoRepository;
    private final UserRepository userRepository;

    /** 🚀 이메일을 기반으로 배송지 목록 조회 */
    @Transactional(readOnly = true)
    public List<DeliveryAddressInfoDto> findByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        return deliveryAddressInfoRepository.findByUser(user).stream()
                .map(info -> new DeliveryAddressInfoDto(
                        info.getAddressName(),
                        info.getZipcode(),
                        info.getStreetAddr(),
                        info.getDetailAddr(),
                        info.getEtc()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeliveryAddressInfoDto findByUserEmailAndAddressName(String email, String addressName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. (email: " + email + ")"));

        System.out.println("📌 [DEBUG] 찾는 사용자: " + user.getEmail());
        System.out.println("📌 [DEBUG] 찾는 배송지 이름: " + addressName);

        Optional<DeliveryAddressInfo> deliveryAddressOpt = deliveryAddressInfoRepository.findByUserAndAddressName(user, addressName);

        if (deliveryAddressOpt.isEmpty()) {
            System.out.println("🚨 [ERROR] 해당 배송지를 찾을 수 없습니다. (addressName: " + addressName + ")");
            throw new IllegalStateException("배송지를 찾을 수 없습니다. (addressName: " + addressName + ")");
        }

        DeliveryAddressInfo deliveryAddress = deliveryAddressOpt.get();

        return new DeliveryAddressInfoDto(
                deliveryAddress.getAddressName(),
                deliveryAddress.getZipcode(),
                deliveryAddress.getStreetAddr(),
                deliveryAddress.getDetailAddr(),
                deliveryAddress.getEtc()
        );
    }


    /** 🚀 배송지 추가 */
    @Transactional
    public void save(String email, DeliveryAddressInfoDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        DeliveryAddressInfo deliveryAddress = DeliveryAddressInfo.builder()
                .user(user)
                .addressName(dto.getAddressName())
                .zipcode(dto.getZipcode())
                .streetAddr(dto.getStreetAddr())
                .detailAddr(dto.getDetailAddr())
                .etc(dto.getEtc())
                .build();

        deliveryAddressInfoRepository.save(deliveryAddress);
    }

    /** 🚀 이메일과 주소명을 기반으로 배송지 수정 */
    @Transactional
    public void updateByEmailAndAddressName(String email, DeliveryAddressInfoDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        DeliveryAddressInfo deliveryAddress = deliveryAddressInfoRepository.findByUserAndAddressName(user, dto.getAddressName())
                .orElseThrow(() -> new IllegalStateException("배송지를 찾을 수 없습니다."));

        deliveryAddress.updateDeliveryAddress(
                dto.getAddressName(),
                dto.getZipcode(),
                dto.getStreetAddr(),
                dto.getDetailAddr(),
                dto.getEtc()
        );
    }

    /** 🚀 이메일과 주소명을 기반으로 배송지 삭제 */
    @Transactional
    public void deleteByEmailAndAddressName(String email, String addressName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        DeliveryAddressInfo deliveryAddress = deliveryAddressInfoRepository.findByUserAndAddressName(user, addressName)
                .orElseThrow(() -> new IllegalStateException("배송지를 찾을 수 없습니다."));

        deliveryAddressInfoRepository.delete(deliveryAddress);
    }
}
