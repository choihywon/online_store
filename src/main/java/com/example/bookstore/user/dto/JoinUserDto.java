package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinUserDto {
    private String email;
    private String password;
    private String phone;
    private String nickname;
}