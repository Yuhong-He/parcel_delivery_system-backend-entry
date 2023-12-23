package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
v * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/TransportationBroker")
public class TransportBrokerController {
    @Operation(summary = "dispatch a list of new parcels")
    @PostMapping("/newParcels")
    public int newParcels(@Parameter(description = "a list of parcels to be dispatched") @RequestBody List<Parcel> parcels){
        for(Parcel parcel:parcels)
            newParcel(parcel);
        return 0;
    }

    @Operation(summary = "dispatch a single parcel and send a notification of new letters to receivers")
    @PostMapping("/newParcel")
    public int newParcel(@Parameter(description = "a parcel to be dispatched") @RequestBody Parcel parcel){
        return 0;
    }
    /*
    * two components here, may need 2 applications
    * */
}
