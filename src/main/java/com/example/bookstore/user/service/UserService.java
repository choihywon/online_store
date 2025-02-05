package com.example.bookstore.user.service;

import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    //Admin에 들어가있어야 할 거 같...
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::converToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> findById(Long userSeq) {
        return userRepository.findById(userSeq)
                .map(this::convertToDto);
    }

    public void update(String email, String phone, String nickname, String grade, int mileage, char useYn) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.updateUserInfo(phone, nickname, grade, mileage, useYn);
    }
    public void delete(Long userSeq) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.deactiveStatus();
    }

    public boolean checkDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private UserDto convertToDto(User user) {
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
}
