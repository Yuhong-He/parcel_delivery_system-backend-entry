package com.example.parcel_delivery_systembackendentry.dto;

import lombok.Data;

@Data
public class LoginData {
    private String email;
    private String password;
    private int type;
}
