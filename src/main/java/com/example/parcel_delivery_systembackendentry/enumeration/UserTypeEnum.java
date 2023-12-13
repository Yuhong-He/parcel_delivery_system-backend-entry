package com.example.parcel_delivery_systembackendentry.enumeration;

import lombok.Getter;

@Getter
public enum UserTypeEnum {
    Student(1),
    Postman(2),
    MervilleStaff(3),
    EstateServiceStaff(4);

    private final Integer type;

    UserTypeEnum(Integer type) {
        this.type = type;
    }
}
