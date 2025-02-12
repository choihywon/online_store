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

        System.out.println("찾는 사용자: " + user.getEmail());
        System.out.println(" 찾는 배송지 이름: " + addressName);

        Optional<DeliveryAddressInfo> deliveryAddressOpt = deliveryAddressInfoRepository.findByUserAndAddressName(user, addressName);

        if (deliveryAddressOpt.isEmpty()) {
            System.out.println("addressName: " + addressName + ")");
            throw new IllegalStateException("addressName: " + addressName + ")");
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



    @Transactional
    public void save(String email, DeliveryAddressInfoDto dto) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println("사용자 찾음: " + user.getEmail());


            System.out.println("입력된 배송지 정보:");
            System.out.println(" - 배송지 이름: " + dto.getAddressName());
            System.out.println(" - 우편번호: " + dto.getZipcode());
            System.out.println(" - 도로명 주소: " + dto.getStreetAddr());
            System.out.println(" - 상세 주소: " + dto.getDetailAddr());
            System.out.println(" - 기타 정보: " + dto.getEtc());


            Optional<DeliveryAddressInfo> existingAddress = deliveryAddressInfoRepository.findByUserAndAddressName(user, dto.getAddressName());
            if (existingAddress.isPresent()) {
                System.out.println("이미 존재하는 배송지 이름: " + dto.getAddressName());
            } else {

                DeliveryAddressInfo deliveryAddress = DeliveryAddressInfo.builder()
                        .user(user)
                        .addressName(dto.getAddressName())
                        .zipcode(dto.getZipcode())
                        .streetAddr(dto.getStreetAddr())
                        .detailAddr(dto.getDetailAddr())
                        .etc(dto.getEtc())
                        .build();

                System.out.println("배송지 객체 생성 완료");


                deliveryAddressInfoRepository.save(deliveryAddress);
                System.out.println("배송지 저장 완료");
            }
        } else {
            System.out.println("사용자를 찾을 수 없습니다. 이메일: " + email);
        }
    }



    @Transactional
    public void updateByEmailAddressName(String email, String originalAddressName, DeliveryAddressInfoDto dto) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println("사용자 찾음: " + user.getEmail());

            String cleanedOriginalAddressName = originalAddressName.trim();
            String cleanedNewAddressName = dto.getAddressName().trim();

            Optional<DeliveryAddressInfo> optionalDeliveryAddress = deliveryAddressInfoRepository.findByUserAndAddressName(user, cleanedOriginalAddressName);
            if (optionalDeliveryAddress.isPresent()) {
                DeliveryAddressInfo deliveryAddress = optionalDeliveryAddress.get();
                System.out.println("기존 배송지 찾음: " + cleanedOriginalAddressName);

                Optional<DeliveryAddressInfo> duplicateAddress = deliveryAddressInfoRepository.findByUserAndAddressName(user, cleanedNewAddressName);
                if (duplicateAddress.isPresent() && !cleanedOriginalAddressName.equals(cleanedNewAddressName)) {
                    System.out.println("이미 존재하는 배송지 이름: " + cleanedNewAddressName);
                } else {
                    deliveryAddress.updateDeliveryAddress(
                            cleanedNewAddressName,
                            dto.getZipcode(),
                            dto.getStreetAddr(),
                            dto.getDetailAddr(),
                            dto.getEtc()
                    );
                    System.out.println("배송지 정보 업데이트 완료");
                }
            } else {
                System.out.println("이 배송지를 찾을 수 없습니다. 다시 시도해 주세요.");
            }
        } else {
            System.out.println("사용자를 찾을 수 없습니다. 이메일: " + email);
        }
    }





    @Transactional
    public void deleteByEmailAndAddressName(String email, String addressName) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) { // 사용자가 존재하는 경우
            User user = optionalUser.get();
            System.out.println("사용자 찾음: " + user.getEmail());

            Optional<DeliveryAddressInfo> optionalDeliveryAddress = deliveryAddressInfoRepository.findByUserAndAddressName(user, addressName);
            if (optionalDeliveryAddress.isPresent()) { // 배송지가 존재하는 경우
                DeliveryAddressInfo deliveryAddress = optionalDeliveryAddress.get();
                System.out.println("배송지 찾음: " + addressName);

                deliveryAddressInfoRepository.delete(deliveryAddress);
                System.out.println("배송지 삭제 완료");
            } else {
                System.out.println("배송지를 찾을 수 없습니다: " + addressName);
            }
        } else {
            System.out.println("사용자를 찾을 수 없습니다: " + email);
        }
    }

}
