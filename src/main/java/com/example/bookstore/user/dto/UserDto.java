package com.example.bookstore.user.dto;

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
    private char useYn;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
