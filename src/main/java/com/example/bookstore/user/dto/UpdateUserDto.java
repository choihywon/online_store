package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserDto {
    private String phone;
    private String nickname;
}