package com.example.database_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "Default", description = "Default root path controller")
@RequestMapping("/")
public class DefaultController {

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Database System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Database'>GitHub page</a>.</h2>";
    }

}
