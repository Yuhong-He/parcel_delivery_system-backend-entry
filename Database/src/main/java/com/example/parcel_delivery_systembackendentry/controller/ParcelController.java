package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.MongoDB.Parcel;
import com.example.parcel_delivery_systembackendentry.MongoDB.ParcelTrack;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Log")
public class ParcelController {
    @Resource
    private MongoTemplate mongoTemplate;

    @Operation(description = "receive a new Parcel")
    @PostMapping(value = "/newTrack")
    public int newTrail(@Parameter (description = "newParcel") @RequestBody ParcelTrack data) {
        //implemented using mom
        return 0;
    }

    @Operation(description = "update a parcelTrack")
    @PutMapping("/updateTrack")
    public int updateTrack(@Parameter(description = "updated ParcelTrack") @RequestBody ParcelTrack parcelTrack){
        //implemented using mom
        return 0;
    }

    @Operation(description = "get parcelTracks of a receiver")
    @GetMapping(value = "/getReceiverTrack")
    public List<Parcel> getReceiverTrack(@Parameter(description = "user's ID") @RequestParam int receiverId) {
        Query query = new Query(Criteria.where("student").is(receiverId));
        return mongoTemplate.find(query, Parcel.class);
    }


}
