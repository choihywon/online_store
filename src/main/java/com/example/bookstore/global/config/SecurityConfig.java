package com.example.bookstore.global.config;

import com.example.bookstore.user.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/users/join", "/users/signup", "/users/login", "/users/check-email", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/users/mypage").hasAuthority("ROLE_USER")  // ✅ ROLE_USER 그대로 사용
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")  // ✅ ROLE_ADMIN 그대로 사용
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/users/login")  // 로그인 페이지
                        .usernameParameter("email") // 로그인 시 이메일 사용
                        .passwordParameter("password") // 비밀번호 설정
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 메인 페이지로 이동
                        .failureUrl("/users/login?error=true") // 로그인 실패 시 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")  // ✅ 로그아웃 URL 설정
                        .logoutSuccessUrl("/")  // 로그아웃 후 메인 페이지로 이동
                        .permitAll()
                )
                .userDetailsService(userDetailsService); // 커스텀 UserDetailsService 설정

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
