package com.example.entry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Email Data")
public class Email {

    @NotNull
    @Schema(description = "Email Destination", example = "yuhong.he@ucdconnect.ie")
    private String to;

    @NotNull
    @Schema(description = "Email Subject", example = "Email Verification")
    private String subject;

    @NotNull
    @Schema(description = "Email HTML Body", example = "<p>Hello World</p>")
    private String htmlBody;

    @Override
    public String toString() {
        return to + ";;" + subject + ";;" + htmlBody;
    }

}
