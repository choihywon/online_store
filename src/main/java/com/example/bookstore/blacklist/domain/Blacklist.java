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
    @JoinColumn(name = "user_seq")
    private User user;

    @Column(nullable = false)
    private String reason;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "unleashed_at")
    private LocalDateTime unleashedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blacklisted_by")
    private User blacklistedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unleashed_by")
    private User unleashedBy;

    @PrePersist
    protected void onCreate() {
        this.blacklistedAt = LocalDateTime.now();
    }

    public void unblacklist(User admin) {
        this.unleashedBy = admin;
        this.unleashedAt = LocalDateTime.now();
    }
}
