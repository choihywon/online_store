package com.example.bookstore.deliveryaddress.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryAddressInfoDto {
    private String addressName;  // ✅ destName → addressName 변경
    private String zipcode;
    private String streetAddr;   // ✅ roadAddress → streetAddr 변경
    private String detailAddr;   // ✅ addressDetail → detailAddr 변경
    private String etc;

    public DeliveryAddressInfoDto(String addressName, String zipcode, String streetAddr, String detailAddr, String etc) {
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
    }
}
