package com.example.parcel_delivery_systembackendentry.MongoDB;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.parcel_delivery_systembackendentry.dto.ParcelTrackWithParcelID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parcel Track Information")
public class ParcelTrack {
    @NotNull
    @Schema(description = "Track Description", example = "Estate Service created parcel label")
    @TableField("description")
    private String description;

    @NotNull
    @Schema(description = "Postman User ID", example = "2")
    @TableField("postman")
    private Integer postman;

    @NotNull
    @Schema(description = "Assigned to Merville Room?", example = "true")
    @TableField("merville_room")
    private boolean merville_room;

    @NotNull
    @Schema(description = "User ID who create this Track", example = "4")
    @TableField("create_by")
    private Integer create_by;

    @NotNull
    @Schema(description = "Track Created Time", example = "2023-12-17 14:23:17")
    @TableField("create_at")
    private Timestamp create_at;

    public ParcelTrack(ParcelTrackWithParcelID parcelTrackWithParcelID) {
        this.description = parcelTrackWithParcelID.getDescription();
        this.postman = parcelTrackWithParcelID.getPostman();
        this.merville_room = parcelTrackWithParcelID.isMerville_room();
        this.create_at = parcelTrackWithParcelID.getCreate_at();
        this.create_by = parcelTrackWithParcelID.getCreate_by();
    }
}
