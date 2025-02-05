package com.example.bookstore.user.domain;


import com.example.bookstore.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;
    @Column(nullable = false, unique = true)
    public String nickname;
    private String grade;
    @Column(nullable = false)
    private int mileage;
    @Column(nullable = false)
    private char useYn;  //블랙 유무 확인

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public static User createUser(String email, String phone, String password, String nickname, String grade) {
        return new User(email, phone, password, nickname, grade, 0, 'Y', UserRole.ROLE_USER);
    }

    private User(String email, String phone, String password, String nickname, String grade, int mileage, char useYn, UserRole role) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
        this.grade = grade;
        this.mileage = mileage;
        this.useYn = useYn;
        this.role = role;
    }

    public void updateUserInfo(String phone, String nickname, String grade, int mileage, char userYn) {
        this.phone = phone;
        this.nickname = nickname;
        this.grade = grade;
        this.mileage = mileage;
        this.useYn = userYn;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void activeStatus() {
        this.useYn = 'Y';
    }

    public void deactiveStatus() {
        this.useYn = 'N';
    }


}
