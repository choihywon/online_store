package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserDto {
    private String phone;
    private String nickname;
}