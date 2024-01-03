package org.example.receiver.dto;

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
public class ParcelWithLatestTrack {

    @NotNull
    @Schema(description = "Parcel ID", example = "9aed31b2-1577-4c9c-9778-f2101fa4cf46")
    @MongoId
    private String id;

    @NotNull
    @Schema(description = "Parcel Type", example = "2 (Regular Mail)")
    private int type;

    @NotNull
    @Schema(description = "Track Description", example = "Estate Service created parcel label")
    private String lastUpdateDesc;

    @NotNull
    @Schema(description = "Track Created Time", example = "2023-12-17 14:23:17")
    private String lastUpdateAt;

}
