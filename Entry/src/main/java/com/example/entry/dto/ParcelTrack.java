package com.example.entry.dto;

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
}
