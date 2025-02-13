package com.example.bookstore.blacklist.domain;

import com.example.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "blacklists")
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_seq")
    private Long blacklistSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user; // ✅ 블랙리스트 대상 사용자

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(name = "blacklisted_at", nullable = false, updatable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "unleashed_at")
    private LocalDateTime unleashedAt;

    @Column(name = "blacklisted_by", nullable = false)
    private String blacklistedBy; // ✅ 관리자 이메일 저장

    @Column(name = "unleashed_by")
    private String unleashedBy; // ✅ 해제 관리자 이메일 저장

    /** ✅ 블랙리스트 등록 시 `blacklistedAt` 자동 설정 */
    @PrePersist
    protected void onCreate() {
        this.blacklistedAt = LocalDateTime.now();
    }
}
