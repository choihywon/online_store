package com.example.bookstore.user.service;

import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.domain.User;
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

    //이메일로 회원 조회
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserDto(
                        user.getEmail(),
                        user.getPhone(),
                        user.getNickname(),
                        user.getGrade(),
                        user.getMileage(),
                        user.getUseYn(),
                        user.getCreatedAt(),
                        user.getLastModifiedAt()
                ));
    }




    //회원 정보 수정
    public void update(Long id, String phone, String nickname) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.updateUserInfo(phone, nickname);
    }

    //회원 탈퇴
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        user.deactivate();
    }
}