package com.example.estate.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "User Information")
public class User {

    @NotNull
    @Schema(description = "Account User ID", example = "6")
    private Integer id;

    @NotNull
    @Schema(description = "Account Username", example = "Alex Smith")
    private String username;

    @NotNull
    @Schema(description = "Account Password", example = "123456")
    private String password;

    @NotNull
    @Schema(description = "Account Email Address", example = "alex.smith@ucdconnect.ie")
    private String email;

    @NotNull
    @Schema(description = "Account User Type", example = "1 (Student)")
    private int type;

}
