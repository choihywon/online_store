package com.example.bookstore.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private String grade = "BASIC";

    @Column(nullable = false)
    private int mileage = 0;

    @Column(nullable = false)
    private char useYn = 'Y';

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.ROLE_USER;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime lastModifiedAt = LocalDateTime.now();

    //정적 팩토리 메서드 (회원가입 시 사용)
    public static User createUser(String email, String password, String phone, String nickname) {
        return new User(null, email, password, phone, nickname, "BASIC", 0, 'Y', UserRole.ROLE_USER, LocalDateTime.now(), LocalDateTime.now());
    }

    //회원 정보 업데이트
    public void updateUserInfo(String phone, String nickname) {
        this.phone = phone;
        this.nickname = nickname;
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 회원 탈퇴 (비활성화)
    public void deactivate() {
        this.useYn = 'N';
    }
}