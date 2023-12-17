package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.message.MQ;
import org.springframework.beans.factory.annotation.Autowired;
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
    public int newParcel(@RequestBody Parcel data){
        try {
            MQ.sendLog(null);
            MQ.notifyReceiver(null,null);
        }catch (Exception e){
            //handle exception here
        }

        return 0;
    }

    /*
    * get all parcels
    * more details of the storage admin to be confirmed*/
    @GetMapping("/getParcels")
    public List<Parcel> getParcels(){
        return null;
    }

    @GetMapping("/parcelsToBeCollected")
    public List<Parcel> parcelsToBeCollected(){
        return null;
    }
    /*
    * parcels for a specific user*/
    @GetMapping("/userParcels")
    public List<Parcel> userParcels(@RequestParam Integer userID){
        return null;
    }

    @PostMapping("/collectionSign")
    public int collectionSign(@RequestParam int receiverID,@RequestParam int parcelID){
        return 0;
    }


}
