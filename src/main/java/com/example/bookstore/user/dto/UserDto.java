package com.example.bookstore.user.dto;

import com.example.bookstore.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class UserDto {
    private String email;
    private String phone;
    private String nickname;
    private String grade;
    private int mileage;
    private Boolean useYn;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}