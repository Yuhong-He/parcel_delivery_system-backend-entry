package com.example.entry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Schema(description = "Parcel Data for Delivery System Staff")
public class ParcelWithStudentInfo {

    @NotNull
    @Schema(description = "Parcel ID", example = "9aed31b2-1577-4c9c-9778-f2101fa4cf46")
    private String id;

    @NotNull
    @Schema(description = "Student Information", example = "{username: \"Alex Smith\", email: \"alex.smith@ucdconnect.ie\"}")
    private StudentInfo studentInfo;

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
    @Schema(description = "Parcel Last Update Description", example = "Estate Service created parcel label")
    private String lastUpdateDesc;

    @NotNull
    @Schema(description = "Parcel Last Update Time", example = "2023-12-17 14:23:17")
    private Timestamp lastUpdateAt;

}
