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


    public void save(AddBlacklistDto addBlacklistDto) {

        User user = userRepository.findByEmail(addBlacklistDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));


        String adminEmail = getLoggedInAdminEmail();
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("관리자가 존재하지 않습니다."));


        Blacklist blacklist = Blacklist.builder()
                .user(user)
                .reason(addBlacklistDto.getReason())
                .blacklistedAt(LocalDateTime.now())
                .blacklistedBy(admin)
                .build();

        user.deactivateUser();
        blacklistRepository.save(blacklist);
    }


    private String getLoggedInAdminEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


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

    @Transactional
    public void unblacklistUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<Blacklist> blacklists = blacklistRepository.findByUser(user);

        if (blacklists.isEmpty()) {
            throw new EntityNotFoundException("블랙리스트에 등록되지 않은 사용자입니다.");
        }

        User admin = getLoggedInAdmin(); //관리자정보!!!!!!!!!!!!!!!!!!

        for (Blacklist blacklist : blacklists) {
            blacklist.unblacklist(admin);
            blacklistRepository.delete(blacklist);
        }

        user.activateUser();
    }

    //어김없이 나온 SecurityContextHolder
    private User getLoggedInAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        return userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("관리자를 찾을 수 없습니다."));
    }
}
