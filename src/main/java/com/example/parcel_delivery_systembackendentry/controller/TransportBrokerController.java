package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
v * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/TransportationBroker")
public class TransportBrokerController {
    @PostMapping("/newParcels")
    public int newParcels(@RequestBody List<Parcel> data){
        return 0;
    }

    /*
    * two components here, may need 2 applications
    * */
}
