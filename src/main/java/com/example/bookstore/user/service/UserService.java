package com.example.bookstore.user.service;

import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.service.DeliveryAddressInfoService;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.domain.UserRole;
import com.example.bookstore.user.dto.*;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryAddressInfoService deliveryAddressInfoService; // 🚀 추가됨

    // 🚀 이메일로 유저 찾기
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        return new UserDto(
                user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.getUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt()
        );
    }

    // 🚀 이메일 중복 확인
    @Transactional(readOnly = true)
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDto joinUser(JoinUserDto joinUserDto) {
        if (checkDuplicateEmail(joinUserDto.getEmail())) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        // ✅ user_hash 생성 (이메일 기반 해시 값)
        String userHash = generateUserHash(joinUserDto.getEmail());

        User user = User.builder()
                .email(joinUserDto.getEmail())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))
                .phone(joinUserDto.getPhone())
                .nickname(joinUserDto.getNickname())
                .grade("NORMAL")
                .mileage(0)
                .useYn('Y')
                .role(UserRole.ROLE_USER)
                .userHash(userHash) // 🔴 필수값 추가
                .build();

        userRepository.save(user);

        // 🚀 기본 배송지 추가
        addDefaultDeliveryAddress(user, joinUserDto);

        return new UserDto(
                user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.getUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt()
        );
    }

    private String generateUserHash(String email) {
        return Integer.toHexString(email.hashCode()); // 간단한 해시 예제
    }

    // 🚀 기본 배송지 추가 메서드 (User 객체 기반)
    private void addDefaultDeliveryAddress(User user, JoinUserDto joinUserDto) {
        DeliveryAddressInfoDto defaultAddress = new DeliveryAddressInfoDto(
                "기본 배송지",
                joinUserDto.getZipcode(),
                joinUserDto.getStreetAddr(),
                joinUserDto.getDetailAddr(),
                joinUserDto.getEtc()
        );

        // 🚀 DeliveryAddressInfoService를 사용하여 기본 배송지 저장
        deliveryAddressInfoService.save(user.getEmail(), defaultAddress);
    }




    // 🚀 회원 정보 수정
    @Transactional
    public void updateUser(String email, UpdateUserDto updateUserDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(updateUserDto.getPhone(), updateUserDto.getNickname());
    }

    // 🚀 회원 탈퇴
    @Transactional
    public void deactivateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        user.deactivateUser(); // Soft Delete (useYn = 'N')
    }
}
