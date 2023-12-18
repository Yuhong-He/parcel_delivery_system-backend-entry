package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.message.MQ;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/*
 * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/Postman")
public class PostmanController {
    /*
     * add the parcel into storage and notify receiver*/
    @PostMapping("/newLetter")
    public int newLetter(@RequestBody Parcel data) {
        try {
            MQ.sendLog(null);
            MQ.notifyReceiver(null);
        } catch (Exception e) {
            //handle exception here
        }
        return 0;
    }

    /*
     * get all parcels
     * more details of the storage admin to be confirmed*/
    @GetMapping("/getLetters")
    public List<Parcel> getLetters(@RequestParam Integer staffID) {
        return null;
    }


    @PostMapping("/collectionSign")
    public int collectionSign(@RequestParam int receiverID, @RequestParam int parcelID) {
        return 0;
    }
}

