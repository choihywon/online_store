package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
