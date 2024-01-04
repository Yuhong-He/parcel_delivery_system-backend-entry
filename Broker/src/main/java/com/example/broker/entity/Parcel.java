package com.example.broker.entity;

import com.example.broker.dto.CreateParcelData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

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
    @Schema(description = "Parcel Last Update Description", example = "Estate Service created parcel label")
    private String lastUpdateDesc;

    @NotNull
    @Schema(description = "Parcel Last Update Time", example = "2023-12-17 14:23:17")
    private Timestamp lastUpdateAt;

    public Parcel(CreateParcelData data, String desc) {
        this.id = UUID.randomUUID().toString();
        this.type = data.getType();
        this.address1 = data.getAddress1();
        this.address2 = data.getAddress2();
        this.student = data.getStudent();
        this.lastUpdateDesc = desc;
    }
}
