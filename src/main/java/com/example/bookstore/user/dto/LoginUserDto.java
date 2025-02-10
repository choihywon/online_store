package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class LoginUserDto {
    private String email;
    private String password;
}