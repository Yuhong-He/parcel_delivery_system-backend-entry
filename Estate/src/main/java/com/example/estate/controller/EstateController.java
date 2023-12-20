package com.example.estate.controller;

import com.example.estate.dto.ParcelInfo;
import com.example.estate.dto.ParcelTrack;
import com.example.estate.entity.Parcel;
import com.example.estate.entity.User;
import com.example.estate.utils.ParcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.estate.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Estate", description = "Estate controller")
@RequestMapping("/estate")
public class EstateController {

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Create a parcel", description = "Allowed Estate Service Staff create a parcel.")
    @PostMapping("/createParcel")
    public void createParcel(@RequestBody ParcelInfo data,
                             @Parameter(description = "Estate Service Staff Account ID") @RequestParam("staff") int staff) {
        if (ParcelUtils.validateParcelCreate(data)) {
            User u = userService.getStudentById(data.getStudent());
            if (u != null) {
                String desc = "Estate Service created parcel label";
                Date dNow = new Date( );
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                ParcelTrack parcelTrack = new ParcelTrack(desc, staff, ft.format(dNow));
                Parcel parcel = new Parcel(data, List.of(parcelTrack));
                mongoTemplate.save(parcel);
            } else {
                log.error("Student " + data.getStudent() + " not exist.");
            }
        } else {
            log.error("Parcel info incomplete: " + data);
        }
    }

}
