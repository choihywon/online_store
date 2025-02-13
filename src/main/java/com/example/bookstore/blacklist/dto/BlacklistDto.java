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
    private String blacklistedBy;
    private String unleashedBy;
}
