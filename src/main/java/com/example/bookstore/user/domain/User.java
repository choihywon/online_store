package com.example.bookstore.user.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;
    private String password;

    private String email;
    private String phone;
    public String nickname;
    private String grade;
    private int mileage;

    private char useYn;  //블랙 유무 확인
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private UserRole role;
}
