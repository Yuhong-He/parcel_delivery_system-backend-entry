package com.example.parcel_delivery_systembackendentry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.parcel_delivery_systembackendentry.dto.CreateParcelData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@TableName("parcel")
public class Parcel {
    @TableId(value="id", type= IdType.ASSIGN_UUID)
    private String id;

    @TableField("type")
    private int type;

    @TableField("address1")
    private String address1;

    @TableField("address2")
    private String address2;

    @TableField("student")
    private int student;

    @TableField("last_update_desc")
    private String lastUpdateDesc;

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
