package com.example.database_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Login Data")
public class LoginData {

    @NotNull
    @Schema(description = "Account Email Address", example = "alex.smith@ucdconnect.ie")
    private String email;

    @NotNull
    @Schema(description = "Account Password", example = "123456")
    private String password;

    @NotNull
    @Schema(description = "Account User Type", example = "1 (Student)")
    private int type;

}
