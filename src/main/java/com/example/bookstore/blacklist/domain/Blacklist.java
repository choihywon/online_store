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
    @JoinColumn(name = "user_seq")  // 'user_seq' 외래키 연결
    private User user;

    @Column(nullable = false)
    private String reason;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "unleashed_at")
    private LocalDateTime unleashedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blacklisted_by")  // 'blacklisted_by' 외래키 연결
    private User blacklistedBy;  // 타입을 String에서 User로 변경

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unleashed_by")  // 'unleashed_by' 외래키 연결
    private User unleashedBy;  // 타입을 String에서 User로 변경

    /** ✅ 블랙리스트 등록 시 `blacklistedAt` 자동 설정 */
    @PrePersist
    protected void onCreate() {
        this.blacklistedAt = LocalDateTime.now();
    }
}
