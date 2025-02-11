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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAddressInfoService {

    private final DeliveryAddressInfoRepository deliveryAddressInfoRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void save(String email, DeliveryAddressInfoDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        // ✅ 기본 배송지가 존재하는지 확인
        if (deliveryAddressInfoRepository.existsByUserAndAddressName(user, dto.getAddressName())) {
            throw new IllegalStateException("이미 등록된 배송지 별칭입니다.");
        }

        // ✅ 기본 배송지 추가
        DeliveryAddressInfo deliveryAddressInfo = DeliveryAddressInfo.builder()
                .user(user)
                .addressName(dto.getAddressName())
                .zipcode(dto.getZipcode())
                .streetAddr(dto.getStreetAddr())
                .detailAddr(dto.getDetailAddr())
                .etc(dto.getEtc())
                .build();

        deliveryAddressInfoRepository.save(deliveryAddressInfo);
    }

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

    @Transactional
    public void delete(Long id) {
        deliveryAddressInfoRepository.deleteById(id);
    }
}
