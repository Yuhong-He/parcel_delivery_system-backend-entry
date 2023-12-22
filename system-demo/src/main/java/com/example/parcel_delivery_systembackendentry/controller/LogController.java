package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.service.ParcelTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*provide basic functions here
 * more functions to add along with development
 * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/Log")
public class LogController {
    @Resource
    private ParcelTrackService parcelTrackService;

    @Operation(description = "receive a new Parcel")
    @PostMapping(value = "/newTrail")
    @Parameters(value = {@Parameter (description = "newParcel")})
    public int newTrail(@RequestBody ParcelTrack data) {
        return 0;
    }

    @Operation(description = "update a parcelTrack")
    @PutMapping("/updateTrack")
    @Parameters(value = {@Parameter(description = "updated ParcelTrack")})
    public int updateTrack(@RequestBody ParcelTrack parcelTrack){
        return 0;
    }

    @Operation(description = "get parcelTracks of a receiver")
    @GetMapping(value = "/getReceiverTrail")
    public List<ParcelTrack> newTrail(@RequestParam int userId) {
        return null;
    }


}
