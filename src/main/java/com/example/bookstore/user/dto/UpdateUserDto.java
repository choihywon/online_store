package com.example.bookstore.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserDto {
    private String phone;
    private String nickname;

    public UpdateUserDto(String phone, String nickname) {
        this.phone = phone;
        this.nickname = nickname;
    }
}
