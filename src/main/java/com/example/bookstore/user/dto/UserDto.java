package com.example.bookstore.user.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
public class UserDto {
    private final String email;
    private final String phone;
    private final String nickname;
    private final String grade;
    private final int mileage;
    //블랙 유무
    private final char useYn;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    public UserDto(String email, String phone, String nickname, String grade, int mileage, char useYn, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.grade = grade;
        this.mileage = mileage;
        this.useYn = useYn;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
