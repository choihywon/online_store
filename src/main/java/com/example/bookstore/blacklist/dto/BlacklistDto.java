package com.example.bookstore.blacklist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistDto {
    private String email;
    private String reason;
    private LocalDateTime blacklistedAt;
    private LocalDateTime unleashedAt;
    private String blacklistedBy;  // ✅ 관리자 이메일 저장
    private String unleashedBy;  // ✅ 해제 관리자 이메일 저장
}
