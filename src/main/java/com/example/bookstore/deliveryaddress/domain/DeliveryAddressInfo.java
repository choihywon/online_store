package com.example.bookstore.deliveryaddress.domain;

import com.example.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery_address_info")
public class DeliveryAddressInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_info_seq")
    private Long deliveryAddressInfoSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(name = "address_name", nullable = false, length = 100)
    private String addressName;

    @Column(name = "zipcode", nullable = false, length = 10)
    private String zipcode;

    @Column(name = "street_addr", nullable = false, length = 255)
    private String streetAddr;

    @Column(name = "detail_addr", nullable = false, length = 255)
    private String detailAddr;

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

    @Builder
    public DeliveryAddressInfo(User user, String addressName, String zipcode,
                               String streetAddr, String detailAddr, String etc) {
        this.user = user;
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

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
