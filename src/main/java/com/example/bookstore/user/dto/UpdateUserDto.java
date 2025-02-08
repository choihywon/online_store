package com.example.bookstore.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserDto {
    private final String email;
    private final String phone;
    private final String nickname;

    public UpdateUserDto(String email, String phone, String nickname) {
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
    }
}
