package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.service.ParcelTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Log")
public class DatabaseController {
    @Resource
    private ParcelTrackService parcelTrackService;

    @Operation(description = "receive a new Parcel")
    @PostMapping(value = "/newTrack")
    public int newTrail(@Parameter (description = "newParcel") @RequestBody ParcelTrack data) {
        return 0;
    }

    @Operation(description = "update a parcelTrack")
    @PutMapping("/updateTrack")
    public int updateTrack(@Parameter(description = "updated ParcelTrack") @RequestBody ParcelTrack parcelTrack){
        return 0;
    }

    @Operation(description = "get parcelTracks of a receiver")
    @GetMapping(value = "/getReceiverTrack")
    public List<ParcelTrack> newTrail(@Parameter(description = "user's ID") @RequestParam int userId) {
        return null;
    }


}
