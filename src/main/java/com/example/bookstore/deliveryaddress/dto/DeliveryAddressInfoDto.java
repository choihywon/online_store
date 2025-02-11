package com.example.bookstore.deliveryaddress.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryAddressInfoDto {
    private String addressName;
    private String zipcode;
    private String streetAddr;
    private String detailAddr;
    private String etc;

    public DeliveryAddressInfoDto(String addressName, String zipcode, String streetAddr, String detailAddr, String etc) {
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.streetAddr = streetAddr;
        this.detailAddr = detailAddr;
        this.etc = etc;
    }
}
