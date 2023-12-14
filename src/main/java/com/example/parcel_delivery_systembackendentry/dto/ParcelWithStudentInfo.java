package com.example.parcel_delivery_systembackendentry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ParcelWithStudentInfo {
    private String id;
    private StudentInfo studentInfo;
    private int type;
    private String address1;
    private String address2;
    private String lastUpdateDesc;
    private Timestamp lastUpdateAt;
}
