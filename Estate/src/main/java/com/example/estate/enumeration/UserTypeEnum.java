package com.example.estate.enumeration;

import lombok.Getter;

@Getter
public enum UserTypeEnum {
    Student(1),
    Postman(2),
    MervilleStaff(3),
    EstateServiceStaff(4);

    private final Integer val;

    UserTypeEnum(Integer val) {
        this.val = val;
    }
}
