package com.example.bookstore.user.service;

import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.service.DeliveryAddressInfoService;
import com.example.bookstore.user.domain.UserRole;
import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DeliveryAddressInfoService deliveryAddressInfoService;
    private final PasswordEncoder passwordEncoder;

    //이메일로 회원 조회
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDto::new);
    }

    //모든 회원 조회
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    //ID로 회원 조회
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::new);
    }

    //회원 가입 (비밀번호 암호화 적용)
    public void save(JoinUserDto joinUserDto) {
        String encodedPassword = passwordEncoder.encode(joinUserDto.getPassword());
        User user = new User(
                joinUserDto.getEmail(), encodedPassword, joinUserDto.getPhone(),
                joinUserDto.getNickname(), "BASIC", 0, 'Y', UserRole.ROLE_USER
        );
        userRepository.save(user);
    }

    //회원 정보 수정
    public void update(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.updateUserInfo(updateUserDto.getPhone(), updateUserDto.getNickname());
    }

    //회원 비밀번호 수정
    public void updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    //회원 탈퇴 (소프트 삭제)
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.deactivate();
    }

    //이메일 중복 확인
    public boolean checkDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    //배송지 목록 조회
    public List<DeliveryAddressInfoDto> getUserDeliveryAddresses(String email) {
        return deliveryAddressInfoService.findByUser(email);
    }
}