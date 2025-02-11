package com.example.bookstore.deliveryaddress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryAddressInfoDto {
    private String addressName;
    private String zipcode;
    private String streetAddr;
    private String detailAddr;
    private String etc;
}