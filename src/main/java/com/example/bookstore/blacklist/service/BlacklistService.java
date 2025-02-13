package com.example.bookstore.blacklist.service;

import com.example.bookstore.blacklist.domain.Blacklist;
import com.example.bookstore.blacklist.dto.AddBlacklistDto;
import com.example.bookstore.blacklist.dto.BlacklistDto;
import com.example.bookstore.blacklist.repository.BlacklistRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final UserRepository userRepository;

    /** ✅ 블랙리스트 등록 */
    @Transactional
    public void save(AddBlacklistDto addBlacklistDto, String adminEmail) {
        User user = userRepository.findByEmail(addBlacklistDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        Blacklist blacklist = Blacklist.builder()
                .user(user)
                .reason(addBlacklistDto.getReason())
                .blacklistedAt(LocalDateTime.now())
                .unleashedAt(null)
                .blacklistedBy(adminEmail) // ✅ 관리자 이메일 저장
                .unleashedBy(null)
                .build();

        user.setUseYn('N'); // ✅ 유저 상태 변경
        blacklistRepository.save(blacklist);
    }

    /** ✅ 블랙리스트 전체 조회 */
    @Transactional(readOnly = true)
    public List<BlacklistDto> findAll() {
        return blacklistRepository.findAll().stream()
                .map(blacklist -> new BlacklistDto(
                        blacklist.getUser().getEmail(),
                        blacklist.getReason(),
                        blacklist.getBlacklistedAt(),
                        blacklist.getUnleashedAt(),
                        blacklist.getBlacklistedBy(), // ✅ 이메일 그대로 반환
                        blacklist.getUnleashedBy() // ✅ 이메일 그대로 반환
                ))
                .collect(Collectors.toList());
    }
}
