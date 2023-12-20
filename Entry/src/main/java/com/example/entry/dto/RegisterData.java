package com.example.entry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Register Data")
public class RegisterData {

    @NotNull
    @Schema(description = "Account Email Address", example = "alex.smith@ucdconnect.ie")
    private String email;

    @NotNull
    @Schema(description = "Account Username", example = "Alex Smith")
    private String username;

    @NotNull
    @Schema(description = "Account Password", example = "123456")
    private String password;

    @NotNull
    @Schema(description = "Account Confirm Password", example = "123456")
    private String password2;

    @NotNull
    @Schema(description = "Account User Type", example = "1 (Student)")
    private int type;

    @NotNull
    @Schema(description = "Verification Code", example = "868944")
    private String code;

}
