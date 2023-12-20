package com.example.entry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "User Information")
@TableName("user")
public class User {

    @NotNull
    @Schema(description = "Account User ID", example = "6")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @NotNull
    @Schema(description = "Account Username", example = "Alex Smith")
    @TableField("username")
    private String username;

    @NotNull
    @Schema(description = "Account Password", example = "123456")
    @TableField("password")
    private String password;

    @NotNull
    @Schema(description = "Account Email Address", example = "alex.smith@ucdconnect.ie")
    @TableField("email")
    private String email;

    @NotNull
    @Schema(description = "Account User Type", example = "1 (Student)")
    @TableField("type")
    private int type;

    public User(String username, String password, String email, int type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
    }
}
