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
                .csrf(csrf -> csrf.disable())  // ✅ CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/users/join", "/users/signup", "/users/login", "/users/check-email", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/users/mypage").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/users/login") // ✅ 로그인 페이지 설정
                        .usernameParameter("email") // ✅ 이메일을 ID로 사용
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true) // ✅ 로그인 성공 시 홈으로 이동
                        .failureUrl("/users/login?error=true") // ✅ 로그인 실패 시 login 페이지에 머무름
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);  // ✅ 사용자 인증 서비스 등록

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 🔹 비밀번호 암호화
    }
}
