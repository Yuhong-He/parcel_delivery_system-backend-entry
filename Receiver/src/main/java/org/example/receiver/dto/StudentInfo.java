package org.example.receiver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Student Basic Information")
public class StudentInfo {

    @NotNull
    @Schema(description = "Student Username", example = "Alex Smith")
    private String username;

    @NotNull
    @Schema(description = "Student Email Address", example = "alex.smith@ucdconnect.ie")
    private String email;
}
