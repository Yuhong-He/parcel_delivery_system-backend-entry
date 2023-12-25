package com.example.database_system.controller;

import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.MongoDB.ParcelRepository;
import com.example.database_system.MongoDB.ParcelTrack;
import com.example.database_system.dto.ParcelTrackWithParcelID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Log")
public class ParcelController {
//    @Resource
//    private MongoTemplate mongoTemplate;

    @Resource
    private ParcelRepository parcelRepository;

    @Operation(description = "receive a new Parcel")
    @PostMapping(value = "/newTrack")
    public int newTrail(@Parameter (description = "newParcel") @RequestBody ParcelTrack data) {
        //implemented using mom
        return 0;
    }

    @Operation(description = "update a parcelTrack")
    @PutMapping("/updateTrack")
    public int updateTrack(@Parameter(description = "updated ParcelTrack with Parcel ID") @RequestBody ParcelTrackWithParcelID parcelTrackWithParcelID) {
        //implemented using mom
        return 0;
    }

    @Operation(description = "get parcelTracks of a receiver",
            responses = {@ApiResponse(description = "a JSONised Slice<Page> object")})
    @GetMapping(value = "/getReceiverTrack")
    public Slice<Parcel> getReceiverTrack(@Parameter(description = "user's ID") @RequestParam int receiverId, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return parcelRepository.findAllByStudent(receiverId, PageRequest.of(pageNumber, pageSize));
    }


}
