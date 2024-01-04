package com.example.database_system.controller;

import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.MongoDB.ParcelRepository;
import com.example.database_system.dto.ParcelTrackWithParcelID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parcel")
public class ParcelController {
    @Resource
    private ParcelRepository parcelRepository;

    @Operation(description = "Create a new Parcel")
    @PostMapping(value = "/newParcel")
    public int newParcel(@Parameter(description = "an Parcel Object") @RequestBody Parcel parcel) {
        // implemented using mom
        parcelRepository.save(parcel);
        return 0;
    }

    @Operation(description = "update a parcelTrack")
    @PutMapping("/updateTrack")
    public int updateTrack(
            @Parameter(description = "updated ParcelTrack with Parcel ID") @RequestBody ParcelTrackWithParcelID parcelTrackWithParcelID) {
        // implemented using mom
        return 0;
    }

    @Operation(description = "get parcels of a receiver", responses = {
            @ApiResponse(description = "a JSONised Slice<Page> object") })
    @GetMapping(value = "/getReceiverParcel")
    public Page<Parcel> getReceiverParcel(@Parameter(description = "receiver's ID") @RequestParam int receiverId,
            @RequestParam int pageNumber) {
        return parcelRepository.findAllByStudent(receiverId, PageRequest.of(pageNumber,10));
    }

    @Operation(description = "get a specific parcel", responses = {
            @ApiResponse(description = "a Parcel Object") })
    @GetMapping(value = "/getParcelWithId/{id}")
    public Parcel getParcelWithId(@Parameter(description = "parcel's ID") @PathVariable String id){
        return parcelRepository.findById(id).orElse(null);
    }
    @Operation(description = "Get all letters for a postman")
    @GetMapping(value = "/getLetters")
    public Page<Parcel> getLetters(@RequestParam int pageNumber) {
//        System.out.println("I am here");
        return parcelRepository.findAllByType(3, PageRequest.of(pageNumber,10));
    }

}
