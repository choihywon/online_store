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
@Table(name = "deliveries_info") // ✅ 정확한 테이블명 설정
public class DeliveryAddressInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveries_info_seq") // ✅ PK 컬럼명 변경 없음
    private Long deliveriesInfoSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(name = "address_name", nullable = false, length = 100) // ✅ dest_name → address_name 변경
    private String addressName;

    @Column(name = "street_addr", nullable = false, length = 255) // ✅ road_address → street_addr 변경
    private String streetAddr;

    @Column(name = "detail_addr", nullable = false, length = 255) // ✅ address_detail → detail_addr 변경
    private String detailAddr;

    @Column(name = "zipcode", nullable = false, length = 10)
    private String zipcode;

    @Column(name = "etc", length = 255)
    private String etc;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    // ✅ 배송지 정보 업데이트 메서드 (컬럼명 변경 반영)
    public void updateDeliveryAddress(String addressName, String zipcode,
                                      String streetAddr, String detailAddr, String etc) {
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
