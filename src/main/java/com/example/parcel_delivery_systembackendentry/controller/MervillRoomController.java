package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.message.MQ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/MervillRoom")
public class MervillRoomController {
    /*
     * add the parcel into storage and notify receiver*/
    @PostMapping("/newParcel")
    public int newParcel(@RequestBody Parcel data) {
        try {
            MQ.sendLog(null);
            MQ.notifyReceiver(null,0);
        } catch (Exception e) {
            //handle exception here
        }

        return 0;
    }

    /*
     * get all Packages
     * more details of the storage admin to be confirmed*/
    @GetMapping("/getPackages")
    public List<Parcel> getPackages() {
        return null;
    }

    @GetMapping("/PackagesToBeCollected")
    public List<Parcel> PackagesToBeCollected() {
        return null;
    }

    /*
     * Packages for a specific user*/
    @GetMapping("/userPackages")
    public List<Parcel> userPackages(@RequestParam Integer userID) {
        return null;
    }

    @PostMapping("/collectionSign")
    public int collectionSign(@RequestParam int receiverID, @RequestParam int parcelID) {
        return 0;
    }


}
