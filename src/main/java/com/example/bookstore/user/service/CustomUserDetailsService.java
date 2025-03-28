//package com.example.bookstore.user.service;
//
//import com.example.bookstore.user.domain.User;
//import com.example.bookstore.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("이메일: " + email);
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    System.out.println("로그인 실패 " + email);
//                    return new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다: " + email);
//                });
//
//        System.out.println("로그인 성공 : " + user.getEmail());
//        System.out.println("저장된 역할 " + user.getRole().name());
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .authorities(user.getRole().name()) //
//                .build();
//
//    }
//}
package com.example.bookstore.user.service;

import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
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
        System.out.println("이메일: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("로그인 실패 - 존재하지 않는 이메일: " + email);
                    return new UsernameNotFoundException("해당 이메일을 못 찾음 " + email);
                });


        if (user.getUseYn() == 'N') {
            System.out.println("로그인 실패 - 비활성화된 계정: " + email);
            throw new DisabledException("비활성화된 계정입니다. 관리자에게 문의하세요.");
        }

        System.out.println("로그인 성공 : " + user.getEmail());
        System.out.println("저장된 역할 " + user.getRole().name());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name()) //
                .build();
    }
}
