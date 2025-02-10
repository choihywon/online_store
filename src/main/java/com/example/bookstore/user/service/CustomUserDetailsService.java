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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다: " + email));

        System.out.println("✅ UserDetailsService에서 찾은 사용자: " + user.getEmail()); // ✅ 로그 추가

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())  // ✅ username을 email로 설정
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}