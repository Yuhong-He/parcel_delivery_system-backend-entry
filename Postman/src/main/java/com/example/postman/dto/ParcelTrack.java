package com.example.postman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parcel Track Information")
public class ParcelTrack {
    @NotNull
    @Schema(description = "Track Description", example = "Estate Service created parcel label")
    private String description;

    @NotNull
    @Schema(description = "Postman User ID", example = "2")
    private Integer postman;

    @NotNull
    @Schema(description = "Assigned to Merville Room?", example = "true")
    private boolean merville_room;

    @NotNull
    @Schema(description = "User ID who create this Track", example = "4")
    private Integer create_by;

    @NotNull
    @Schema(description = "Track Created Time", example = "2023-12-17 14:23:17")
    private String create_at;


    public ParcelTrack(String description, Integer create_by, String create_at) {
        this.description = description;
        this.postman = -1;
        this.merville_room = false;
        this.create_by = create_by;
        this.create_at = create_at;
    }
    public ParcelTrack(ParcelTrackWithParcelID parcelTrackWithParcelID) {
        this.description = parcelTrackWithParcelID.getDescription();
        this.postman = parcelTrackWithParcelID.getPostman();
        this.merville_room = parcelTrackWithParcelID.isMerville_room();
        this.create_at = parcelTrackWithParcelID.getCreate_at();
        this.create_by = parcelTrackWithParcelID.getCreate_by();
    }
}
