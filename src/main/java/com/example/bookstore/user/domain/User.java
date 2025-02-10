package com.example.bookstore.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_hash", nullable = false, unique = true, length = 255)
    private String userHash;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "grade", nullable = false, length = 20)
    private String grade;

    @Column(name = "mileage", nullable = false)
    private int mileage;

    @Column(name = "use_yn", nullable = false)
    private boolean useYn; // ✅ boolean 타입의 활성화 여부 필드

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt;

    @PrePersist
    protected void onCreate() {
        this.userHash = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.useYn = true; //기본적으로 true로 설정
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    @Builder
    public User(String email, String password, String phone, String nickname, String grade, int mileage, boolean useYn, UserRole role) {
        this.userHash = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.grade = grade;
        this.mileage = mileage;
        this.useYn = useYn; //Builder에 포함
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    //비밀번호 변경
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }

    //사용자 정보 업데이트
    public void updateUserInfo(String phone, String nickname) {
        this.phone = phone;
        this.nickname = nickname;
        this.lastModifiedAt = LocalDateTime.now();
    }

    //회원 비활성화 (Soft Delete)
    public void deactivateUser() {
        this.useYn = false;
    }
}
