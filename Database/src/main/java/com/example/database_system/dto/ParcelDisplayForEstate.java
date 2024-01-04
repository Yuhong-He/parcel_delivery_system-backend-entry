package com.example.database_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Parcel with Latest Track Information")
public class ParcelDisplayForEstate {

    @NotNull
    @Schema(description = "Parcel ID", example = "9aed31b2-1577-4c9c-9778-f2101fa4cf46")
    @MongoId
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
    @Schema(description = "Track Description", example = "Estate Service created parcel label")
    private String lastUpdateDesc;

    @NotNull
    @Schema(description = "Track Created Time", example = "2023-12-17 14:23:17")
    private String lastUpdateAt;

}
