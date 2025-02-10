package com.example.bookstore.user.dto;

import com.example.bookstore.user.domain.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserDto {
    private final String email;
    private final String phone;
    private final String nickname;
    private final String grade;
    private final int mileage;
    private final char useYn;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.nickname = user.getNickname();
        this.grade = user.getGrade();
        this.mileage = user.getMileage();
        this.useYn = user.getUseYn();
        this.createdAt = user.getCreatedAt();
        this.lastModifiedAt = user.getLastModifiedAt();
    }
}