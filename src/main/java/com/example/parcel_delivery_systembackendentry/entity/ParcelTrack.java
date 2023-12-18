package com.example.parcel_delivery_systembackendentry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName("parcel_track")
public class ParcelTrack {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("parcel_id")
    private String parcel_id;

    @TableField("description")
    private String description;

    @TableField("postman")
    private Integer postman;

    @TableField("merville_room")
    private boolean merville_room;

    @TableField("create_by")
    private Integer create_by;

    @TableField("create_at")
    private Timestamp create_at;

    public ParcelTrack(String parcel_id, String description, Integer create_by) {
        this.parcel_id = parcel_id;
        this.description = description;
        this.create_by = create_by;
    }
}
