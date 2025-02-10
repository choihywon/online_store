package com.example.bookstore.deliveryaddress.domain;


import com.example.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_address_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DeliveryAddressInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryAddressInfoSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(nullable = false)
    private String addressName;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String streetAddr;

    @Column(nullable = false)
    private String detailAddr;

    private String etc;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    //배송지 정보 생성
    public static DeliveryAddressInfo createAddress(User user, String addressName, String zipcode, String streetAddr, String detailAddr, String etc) {
        return new DeliveryAddressInfo(user, addressName, zipcode, streetAddr, detailAddr, etc);
    }

    private DeliveryAddressInfo(User user, String addressName, String zipcode, String streetAddr, String detailAddr, String etc) {
        this.user = user;
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
    }

    //배송지 정보 수정
    public void updateAddress(String addressName, String zipcode, String streetAddr, String detailAddr, String etc) {
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
    }
}