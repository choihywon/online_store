package com.example.bookstore.user.dto;

import lombok.Getter;

@Getter
public class JoinUserDto {
    private String email;
    private String password;
    private String phone;
    private String nickname;
    private String zipcode;
    private String streetAddr;
    private String detailAddr;

    public JoinUserDto(String email, String password, String phone, String nickname, String zipcode, String streetAddr, String detailAddr) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
    }
}
