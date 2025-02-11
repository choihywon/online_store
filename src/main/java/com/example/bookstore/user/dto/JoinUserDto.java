package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinUserDto {
    private String email;
    private String password;
    private String phone;
    private String nickname;
    private String zipcode;
    private String streetAddr;
    private String detailAddr;
    private String etc;
}