package com.example.postman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Custom Pagination Data")
public class CustomPage {

    @NotNull
    @Schema(description = "Pagination Records", example = "[{},{},{}]")
    private List<Parcel> records;

    @NotNull
    @Schema(description = "Number of Records", example = "20")
    private long total;

    @NotNull
    @Schema(description = "Page Size", example = "10")
    private long size;

    @NotNull
    @Schema(description = "Current Page Number", example = "1")
    private long current;

    @NotNull
    @Schema(description = "Total Page Number", example = "5")
    private long pages;

}
