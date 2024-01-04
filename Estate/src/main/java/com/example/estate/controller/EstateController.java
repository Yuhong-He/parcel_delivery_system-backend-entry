package com.example.estate.controller;

import com.example.estate.dto.*;
import com.example.estate.entity.Parcel;
import com.example.estate.entity.ParcelTrack;
import com.example.estate.entity.User;
import com.example.estate.utils.ParcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@Tag(name = "Estate", description = "Estate controller")
@RequestMapping("/")
public class EstateController {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${database.address}")
    private String database = "";

    @Value("${broker.address}")
    private String broker = "";

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Estate Service Staff System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Estate'>GitHub page</a>.</h2>";
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Create a parcel", description = "Allowed Estate Service Staff create a parcel.")
    @PostMapping("/createParcel")
    public void createParcel(@RequestBody ParcelInfo data,
                             @Parameter(description = "Estate Service Staff Account ID") @RequestParam("staff") int staff) {
        if (ParcelUtils.validateParcelCreate(data)) {
            User u = restTemplate.getForObject(database + "/user/getStudentById?id=" + data.getStudent(), User.class);
            if (u != null) {
                String desc = "Estate Service created parcel label";
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                ParcelTrack parcelTrack = new ParcelTrack(desc, staff, formattedDateTime);
                Parcel parcel = new Parcel(data, List.of(parcelTrack));
                restTemplate.postForEntity(database + "/parcel/newParcel", parcel, Integer.class);
                ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(broker + "/distribute", parcel, Integer.class);
                if (responseEntity.getBody() != null && responseEntity.getBody() == 0) {
                    log.info("Broker distribute success");
                } else {
                    log.error("Broker distribute failed");
                }
            } else {
                log.error("Student " + data.getStudent() + " not exist.");
            }
        } else {
            log.error("Parcel info incomplete: " + data);
        }
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/text")})
    @Operation(summary = "List parcels", description = "Allowed Estate Service Staff list parcels.")
    @GetMapping("/list")
    public String getParcelList(@Parameter(description = "Page Number") @RequestParam("page") Integer pageNo) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> responseEntity = template.getForEntity(database + "/parcel/getAllParcels?pageNo=" + pageNo, String.class);
        return responseEntity.getBody();
    }

}
