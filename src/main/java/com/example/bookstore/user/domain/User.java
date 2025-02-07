package com.example.bookstore.user.domain;

import com.example.bookstore.user.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private char useYn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    //정적 팩토리 메서드 (회원 생성)
    public static User createUser(String email, String password, String phone, String nickname, UserRole role) {
        return new User(email, password, phone, nickname, "BASIC", 0, 'Y', role);
    }

    //생성자 (생성 제한 & 정적 팩토리 메서드 사용)
    private User(String email, String password, String phone, String nickname, String grade, int mileage, char useYn, UserRole role) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.grade = grade;
        this.mileage = mileage;
        this.useYn = useYn;
        this.role = role;
    }

    //회원 정보 수정
    public void updateUserInfo(String phone, String nickname) {
        this.phone = phone;
        this.nickname = nickname;
    }

    //비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    //회원 탈퇴 (비활성화)
    public void deactivate() {
        this.useYn = 'N';
    }
}