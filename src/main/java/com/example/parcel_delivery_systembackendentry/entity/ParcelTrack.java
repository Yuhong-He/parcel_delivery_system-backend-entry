package com.example.parcel_delivery_systembackendentry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Schema(description = "Parcel Track Information")
@TableName("parcel_track")
public class ParcelTrack {

    @NotNull
    @Schema(description = "Track ID", example = "25")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @NotNull
    @Schema(description = "Parcel ID", example = "9aed31b2-1577-4c9c-9778-f2101fa4cf46")
    @TableField("parcel_id")
    private String parcel_id;

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

    public ParcelTrack(String parcel_id, String description, Integer create_by) {
        this.parcel_id = parcel_id;
        this.description = description;
        this.create_by = create_by;
    }
}
