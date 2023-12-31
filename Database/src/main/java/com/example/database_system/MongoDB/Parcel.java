package com.example.database_system.MongoDB;

import com.example.database_system.dto.ParcelInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Schema(description = "Parcel Information")
@Document("parcel")
public class Parcel {

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
    @Schema(description = "Parcel Tracks", example = "{}, {}, {}")
    private List<ParcelTrack> tracks;

    public Parcel(ParcelInfo data, List<ParcelTrack> tracks) {
        this.id = UUID.randomUUID().toString();
        this.type = data.getType();
        this.address1 = data.getAddress1();
        this.address2 = data.getAddress2();
        this.student = data.getStudent();
        this.tracks = tracks;
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