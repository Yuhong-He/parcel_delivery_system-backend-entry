package com.DSGroup13.ucdParcel.controller;

import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.message.MQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Postman")
public class PostmanController {
    /*
     * add the parcel into storage and notify receiver*/
    @PostMapping("/newLetter")
    public int newLetter(@RequestBody Parcel data){
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
    @GetMapping("/getLetters")
    public List<Parcel> getLetters(@RequestParam Integer staffID){
        return null;
    }



    @PostMapping("/collectionSign")
    public int collectionSign(@RequestParam int receiverID,@RequestParam int parcelID){
        return 0;
    }
}
