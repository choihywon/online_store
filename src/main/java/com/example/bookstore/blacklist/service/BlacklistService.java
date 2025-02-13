package com.example.bookstore.blacklist.service;

import com.example.bookstore.blacklist.domain.Blacklist;
import com.example.bookstore.blacklist.dto.AddBlacklistDto;
import com.example.bookstore.blacklist.dto.BlacklistDto;
import com.example.bookstore.blacklist.repository.BlacklistRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    /** 블랙리스트 등록 */
    public void save(AddBlacklistDto addBlacklistDto) {
        // 유저 정보 조회
        User user = userRepository.findByEmail(addBlacklistDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        // 로그인한 관리자의 이메일 가져오기
        String adminEmail = getLoggedInAdminEmail();  // 로그인한 사용자의 이메일
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("관리자가 존재하지 않습니다."));

        // 블랙리스트 객체 생성
        Blacklist blacklist = Blacklist.builder()
                .user(user)  // 블랙리스트 대상 유저 설정
                .reason(addBlacklistDto.getReason())  // 블랙리스트 사유 설정
                .blacklistedAt(LocalDateTime.now())  // 현재 시간으로 설정
                .blacklistedBy(admin)  // 관리자 User 객체 사용
                .build();

        // 유저 상태 변경
        user.deactivateUser();  // 유저 상태 변경 (비활성화)
        blacklistRepository.save(blacklist); // 블랙리스트 저장
    }

    /** 로그인한 관리자의 이메일을 가져오는 메서드 */
    private String getLoggedInAdminEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();  // 로그인한 사용자의 이메일
    }

    /** 블랙리스트 전체 조회 */
    @Transactional(readOnly = true)
    public List<BlacklistDto> findAll() {
        return blacklistRepository.findAll().stream()
                .map(blacklist -> new BlacklistDto(
                        blacklist.getUser().getEmail(),
                        blacklist.getReason(),
                        blacklist.getBlacklistedAt(),
                        blacklist.getUnleashedAt(),
                        blacklist.getBlacklistedBy().getEmail(),  // User 객체에서 이메일 가져오기
                        blacklist.getUnleashedBy() != null ? blacklist.getUnleashedBy().getEmail() : null  // UnleashedBy도 User에서 이메일 가져오기
                ))
                .collect(Collectors.toList());
    }
}
