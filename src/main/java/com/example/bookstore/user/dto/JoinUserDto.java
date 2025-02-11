package com.example.bookstore.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String streetAddr;    // ✅ 올바른 필드명 사용
    private String detailAddr;    // ✅ 올바른 필드명 사용
    private String etc;
}
