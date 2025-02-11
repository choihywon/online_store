package com.example.bookstore.deliveryaddress.domain;


import com.example.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "deliveries_info") // 🚀 테이블 매핑
public class DeliveryAddressInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_seq") // 🚀 기본 키
    private Long id;

    // 🚀 User와의 연관 관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false) // `user_seq`를 외래키로 지정
    private User user;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "street_addr", nullable = false)
    private String streetAddr;

    @Column(name = "detail_addr")
    private String detailAddr;

    @Column(name = "etc")
    private String etc;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt;

    // 🚀 엔티티 생성 시 자동으로 `created_at`과 `last_modified_at` 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 🚀 엔티티 수정 시 `last_modified_at` 갱신
    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 🚀 빌더 패턴에서 `User` 객체를 직접 받도록 수정
    @Builder
    public DeliveryAddressInfo(User user, String addressName, String zipcode,
                               String streetAddr, String detailAddr, String etc) {
        this.user = user;
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
    }

    // 🚀 배송지 정보 업데이트 메서드
    public void updateDeliveryAddress(String addressName, String zipcode, String streetAddr,
                                      String detailAddr, String etc) {
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
