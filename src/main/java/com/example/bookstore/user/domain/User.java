package com.example.bookstore.user.domain;

import com.example.bookstore.user.domain.UserRole;
import com.example.bookstore.user.dto.UpdateUserDto;
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

    //회원 정보 수정 (DTO 활용)
    public void updateUserInfo(UpdateUserDto updateUserDto) {
        this.phone = updateUserDto.getPhone();
        this.nickname = updateUserDto.getNickname();
    }

    //회원 탈퇴 (소프트 삭제)
    public void deactivate() {
        this.useYn = 'N';
    }
}