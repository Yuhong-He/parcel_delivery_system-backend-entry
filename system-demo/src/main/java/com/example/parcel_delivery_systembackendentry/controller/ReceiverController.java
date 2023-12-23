package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/Receiver")
public class ReceiverController {
    @Operation(summary = "Get all parcels for a specific Receiver and filter the parcels to be processed")
    @GetMapping("/getHistory")
    public List<ParcelTrack> getHistory(@Parameter(description = "the receiver's ID") @RequestParam int receiverID) {
        return null;
    }

    @Operation(summary = "to confirm a letter's address or to confirm the collection of a parcel")
    @PostMapping("/confirm")
    public int confirm(@Parameter(description = "the id of the parcel to be confirmed") @RequestParam int parcelID) {
        return 0;
    }
    /*Define DeliverCallback callBack to customize how client deal with new notification
    run MQ.consumeNotification(callBack); in the SpringBoot Application*/

}
