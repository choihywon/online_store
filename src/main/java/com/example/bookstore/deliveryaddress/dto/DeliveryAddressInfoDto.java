package com.example.bookstore.deliveryaddress.dto;

import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;

public class DeliveryAddressInfoDto {
    private final String addressName;
    private final String zipcode;
    private final String streetAddr;
    private final String detailAddr;
    private final String etc;

    public DeliveryAddressInfoDto(DeliveryAddressInfo address) {
        this.addressName = address.getAddressName();
        this.zipcode = address.getZipcode();
        this.streetAddr = address.getStreetAddr();
        this.detailAddr = address.getDetailAddr();
        this.etc = address.getEtc();
    }
}
