package com.example.parcel_delivery_systembackendentry.dto;

import lombok.Data;

@Data
public class CreateParcelData {
    private int type;
    private String address1;
    private String address2;
    private int student;
}
