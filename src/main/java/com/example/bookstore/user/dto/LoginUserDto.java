package com.example.bookstore.user.dto;

import lombok.Getter;

@Getter
public class LoginUserDto {
    private String email;
    private String password;

    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
