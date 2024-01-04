package com.example.broker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Parcel Information")
public class Parcel {

    @NotNull
    @Schema(description = "Parcel ID", example = "9aed31b2-1577-4c9c-9778-f2101fa4cf46")
    private String id;

    @NotNull
    @Schema(description = "Parcel Type", example = "2 (Regular Mail)")
    private int type;

    @NotNull
    @Schema(description = "UCD Residence", example = "Roebuck Hall")
    private String address1;

    @NotNull
    @Schema(description = "Address Detail", example = "House 3, Apt 6, Room 5")
    private String address2;

    @NotNull
    @Schema(description = "User ID of Student", example = "3")
    private int student;

    @NotNull
    @Schema(description = "Parcel Tracks", example = "[{}, {}, {}]")
    private List<ParcelTrack> tracks;

    public void addTrack(ParcelTrack track) {
        if (this.tracks == null) {
            this.tracks = new ArrayList<>();
        }
        this.tracks.add(track);
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", student=" + student +
                ", tracks=" + tracks +
                '}';
    }
}