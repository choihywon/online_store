package com.example.bookstore.user.service;

import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 [로그인 시도] 이메일: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ [로그인 실패] 사용자를 찾을 수 없습니다: " + email);
                    return new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다: " + email);
                });

        System.out.println("✅ [로그인 성공] 사용자 이메일: " + user.getEmail());
        System.out.println("✅ [저장된 역할]: " + user.getRole().name()); // ✅ 역할 확인

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name()) //
                .build();

    }
}
