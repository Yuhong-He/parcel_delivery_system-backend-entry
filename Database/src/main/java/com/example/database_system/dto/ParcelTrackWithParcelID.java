package com.example.database_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Posted ParcelTrack with Parcel id")
public class ParcelTrackWithParcelID {
    private String parcelId;
    private String description;
    private Integer postman;
    private boolean merville_room;
    private Integer create_by;
    private String create_at;
}