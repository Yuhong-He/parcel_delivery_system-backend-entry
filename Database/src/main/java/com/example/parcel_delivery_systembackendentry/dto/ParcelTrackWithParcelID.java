package com.example.parcel_delivery_systembackendentry.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ParcelTrackWithParcelID {
    private String parcelId;
    private String description;
    private Integer postman;
    private boolean merville_room;
    private Integer create_by;
    private Timestamp create_at;
}