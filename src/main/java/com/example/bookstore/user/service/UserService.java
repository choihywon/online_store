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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryAddressInfoService deliveryAddressInfoService; // 🚀 추가됨

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
    @Transactional(readOnly = true)
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email) // ✅ findByEmail() 사용
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        return new UserDto(
                user.getEmail(),
                user.getPhone(),
                user.getNickname(),
                user.getGrade(),
                user.getMileage(),
                user.getUseYn(),
                user.getCreatedAt(),
                user.getLastModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDto joinUser(JoinUserDto joinUserDto) {
        if (checkDuplicateEmail(joinUserDto.getEmail())) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

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
                .userHash(userHash)
                .build();

        userRepository.save(user);

        addDefaultDeliveryAddress(user, joinUserDto);

        return new UserDto(
                user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.getUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt()
        );
    }



    //이게 왜 있냐? Hash를 쿼리에서 지우기도 좀 그렇고 어떻게 할 지 생각하다가 사람들이 ㄹㅇ hash로 변환한걸 보고 복붙함
    private String generateUserHash(String email) {
        return Integer.toHexString(email.hashCode()); // 간단한 해시 예제
    }

    /** ✅ 모든 사용자 조회 */
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getEmail(),
                        user.getPhone(),
                        user.getNickname(),
                        user.getGrade(),
                        user.getMileage(),
                        user.getUseYn(),
                        user.getCreatedAt(),
                        user.getLastModifiedAt()
                ))
                .collect(Collectors.toList());
    }

    /** ✅ 특정 사용자 ID로 상세 조회 */
    @Transactional(readOnly = true)
    public UserDto findUserById(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        return new UserDto(
                user.getEmail(),
                user.getPhone(),
                user.getNickname(),
                user.getGrade(),
                user.getMileage(),
                user.getUseYn(),
                user.getCreatedAt(),
                user.getLastModifiedAt()
        );
    }

    private void addDefaultDeliveryAddress(User user, JoinUserDto joinUserDto) {
        DeliveryAddressInfoDto defaultAddress = new DeliveryAddressInfoDto(
                "기본 배송지",
                joinUserDto.getZipcode(),
                joinUserDto.getStreetAddr(),
                joinUserDto.getDetailAddr(),
                joinUserDto.getEtc()
        );

        deliveryAddressInfoService.save(user.getEmail(), defaultAddress);
    }


    @Transactional
    public void updateUser(String email, UpdateUserDto updateUserDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(updateUserDto.getPhone(), updateUserDto.getNickname());
    }

    @Transactional
    public void deactivateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        user.deactivateUser();
    }
}
