package com.example.parcel_delivery_systembackendentry.dto;

import lombok.Data;

@Data
public class RegisterData {
    private String email;
    private String username;
    private String password;
    private String password2;
    private int type;
    private String code;
}
