package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class LoginUserDto {
    private String email;
    private String password;
}