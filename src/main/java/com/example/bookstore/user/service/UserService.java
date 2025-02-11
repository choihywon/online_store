package com.example.bookstore.user.service;
import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.service.DeliveryAddressInfoService;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.domain.UserRole;
import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.dto.LoginUserDto;
import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryAddressInfoService deliveryAddressInfoService;

    @Transactional
    public UserDto joinUser(JoinUserDto joinUserDto) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(joinUserDto.getEmail())) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 검증
        if (joinUserDto.getPassword() == null || joinUserDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
        }

        // ✅ 회원 정보 저장
        User user = User.builder()
                .email(joinUserDto.getEmail())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))
                .phone(joinUserDto.getPhone())
                .nickname(joinUserDto.getNickname())
                .grade("일반")
                .mileage(0)
                .useYn(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        // ✅ 회원가입 시 기본 배송지 추가
        DeliveryAddressInfoDto defaultAddress = new DeliveryAddressInfoDto(
                "기본 배송지",
                joinUserDto.getZipcode(),
                joinUserDto.getStreetAddr(),   // ✅ 변경된 필드명 사용
                joinUserDto.getDetailAddr(),   // ✅ 변경된 필드명 사용
                joinUserDto.getEtc()
        );
        deliveryAddressInfoService.save(user.getEmail(), defaultAddress);

        return new UserDto(user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.isUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt());
    }



    @Transactional(readOnly = true)
    public UserDto loginUser(LoginUserDto loginUserDto) {
        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return new UserDto(user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.isUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt());
    }


    @Transactional(readOnly = true)
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        System.out.println("✅ findByEmail() 요청 이메일: " + email); // ✅ 로그 추가

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ 사용자를 찾을 수 없습니다: " + email); // ✅ 로그 추가
                    return new IllegalStateException("사용자를 찾을 수 없습니다.");
                });

        return new UserDto(
                user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.isUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt()
        );
    }

    //회원 정보 업데이트 (DTO → Entity 변환 후 저장)
    @Transactional
    public void updateUser(String email, UpdateUserDto updateUserDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(updateUserDto.getPhone(), updateUserDto.getNickname());
    }


    @Transactional
    public void deactivateUser(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        user.deactivateUser(); //Soft Delete (useYn = false)
    }
}