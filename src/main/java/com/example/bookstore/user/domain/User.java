package com.example.bookstore.user.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_hash", nullable = false, updatable = false)
    private String userHash;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private int mileage;

    @Column(name = "use_yn", nullable = false)
    private char useYn; // 'Y' 또는 'N'으로 관리

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.useYn = 'Y';
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void updateUserInfo(String phone, String nickname) {
        this.phone = phone;
        this.nickname = nickname;
        this.lastModifiedAt = LocalDateTime.now();
    }
    public void setUseYn(char useYn) {
        this.useYn = useYn;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void deactivateUser() {
        this.useYn = 'N';
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void activateUser() {
        this.useYn = 'Y';
        this.lastModifiedAt = LocalDateTime.now();
    }
}
