package com.example.bookstore.deliveryaddress.service;

import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
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

    /** 🚀 배송지 목록 조회 */
    @Transactional(readOnly = true)
    public List<DeliveryAddressInfoDto> findByUser(String email) {
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

    // 🚀 배송지 저장 (회원 이메일 기반)
    @Transactional
    public void save(String email, DeliveryAddressInfoDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        DeliveryAddressInfo deliveryAddress = DeliveryAddressInfo.builder()
                .user(user) // 🚀 User 객체로 저장
                .addressName(dto.getAddressName())
                .zipcode(dto.getZipcode())
                .streetAddr(dto.getStreetAddr())
                .detailAddr(dto.getDetailAddr())
                .etc(dto.getEtc())
                .build();

        deliveryAddressInfoRepository.save(deliveryAddress);
    }

    /** 🚀 배송지 수정 */
    @Transactional
    public void update(Long id, DeliveryAddressInfoDto dto) {
        DeliveryAddressInfo deliveryAddressInfo = deliveryAddressInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("배송지를 찾을 수 없습니다."));

        deliveryAddressInfo.updateDeliveryAddress(
                dto.getAddressName(),
                dto.getZipcode(),
                dto.getStreetAddr(),
                dto.getDetailAddr(),
                dto.getEtc()
        );
    }

    /** 🚀 배송지 삭제 */
    @Transactional
    public void delete(Long id) {
        deliveryAddressInfoRepository.deleteById(id);
    }

    /** 🚀 배송지 이름으로 조회 */
    @Transactional(readOnly = true)
    public DeliveryAddressInfoDto findByUserAndAddressName(String email, String addressName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        DeliveryAddressInfo deliveryAddressInfo = deliveryAddressInfoRepository.findByUserAndAddressName(user, addressName)
                .orElseThrow(() -> new IllegalStateException("배송지를 찾을 수 없습니다."));

        return new DeliveryAddressInfoDto(
                deliveryAddressInfo.getAddressName(),
                deliveryAddressInfo.getZipcode(),
                deliveryAddressInfo.getStreetAddr(),
                deliveryAddressInfo.getDetailAddr(),
                deliveryAddressInfo.getEtc()
        );
    }
}
