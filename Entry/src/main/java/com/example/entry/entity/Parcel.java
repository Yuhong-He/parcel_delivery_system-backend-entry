package com.example.entry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entry.dto.CreateParcelData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Schema(description = "Parcel Information")
@TableName("parcel")
public class Parcel {

    @NotNull
    @Schema(description = "Parcel ID", example = "9aed31b2-1577-4c9c-9778-f2101fa4cf46")
    @TableId(value="id", type= IdType.ASSIGN_UUID)
    private String id;

    @NotNull
    @Schema(description = "Parcel Type", example = "2 (Regular Mail)")
    @TableField("type")
    private int type;

    @NotNull
    @Schema(description = "UCD Residence", example = "Roebuck Hall")
    @TableField("address1")
    private String address1;

    @NotNull
    @Schema(description = "Address Detail", example = "House 3, Apt 6, Room 5")
    @TableField("address2")
    private String address2;

    @NotNull
    @Schema(description = "User ID of Student", example = "3")
    @TableField("student")
    private int student;

    @NotNull
    @Schema(description = "Parcel Last Update Description", example = "Estate Service created parcel label")
    @TableField("last_update_desc")
    private String lastUpdateDesc;

    @NotNull
    @Schema(description = "Parcel Last Update Time", example = "2023-12-17 14:23:17")
    @TableField("last_update_at")
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
