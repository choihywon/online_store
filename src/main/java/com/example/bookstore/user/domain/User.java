package com.example.bookstore.user.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users") // DB 테이블명과 일치
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq") // 회원 ID (PK)
    private Long userSeq;

    @Column(name = "user_hash", nullable = false, unique = true, length = 255)
    private String userHash; // 회원 고유 식별자

    @Column(name = "password", nullable = false, length = 255)
    private String password; // 비밀번호

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email; // 이메일

    @Column(name = "phone", nullable = false, length = 20)
    private String phone; // 전화번호

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(name = "grade", nullable = false, length = 20)
    private String grade; // 등급

    @Column(name = "mileage", nullable = false)
    private int mileage; // 마일리지

    @Column(name = "use_yn", nullable = false)
    private char useYn; // 회원 사용 여부

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 회원 생성일시

    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt; // 회원 수정일시

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role; // 역할

    // 회원 정보 생성 시 자동으로 값 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.userHash = UUID.randomUUID().toString(); // 회원 고유 식별자 자동 생성
    }

    // 회원 정보 수정 시 `lastModifiedAt` 업데이트
    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    @Builder
    public User(String email, String password, String phone, String nickname, String grade, int mileage, char useYn, UserRole role) {
        this.userHash = UUID.randomUUID().toString(); // 🔹 회원 고유 식별자 자동 생성
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.grade = grade;
        this.mileage = mileage;
        this.useYn = useYn;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 회원 정보 업데이트 (닉네임 & 전화번호 변경)
    public void updateUserInfo(String phone, String nickname) {
        this.phone = phone;
        this.nickname = nickname;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
