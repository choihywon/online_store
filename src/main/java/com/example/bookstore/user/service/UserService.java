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

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserDto::new);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(UserDto::new);
    }

    public void save(JoinUserDto joinUserDto) {
        if (userRepository.existsByEmail(joinUserDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(joinUserDto.getPassword());
        User user = new User(joinUserDto.getEmail(), encodedPassword, joinUserDto.getPhone(),
                joinUserDto.getNickname(), "BASIC", 0, 'Y', UserRole.ROLE_USER);
        userRepository.save(user);
    }

    public void update(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.updateUserInfo(updateUserDto.getPhone(), updateUserDto.getNickname());
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.deactivate();
    }

    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<DeliveryAddressInfoDto> getUserDeliveryAddresses(String email) {
        return deliveryAddressInfoService.findByUser(email);
    }
}