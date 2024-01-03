package com.example.broker.enumeration;

import lombok.Getter;

@Getter
public enum ParcelTypeEnum {
    PACKAGE(1),
    REGULAR_LETTER(2),
    CRITICAL_LETTER(3);

    private final int value;

    ParcelTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
