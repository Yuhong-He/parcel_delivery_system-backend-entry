package com.example.broker.controller;


import com.example.broker.entity.Parcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import akka.actor.ActorRef;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/*
v * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/TransportationBroker")
public class TransportBrokerController {
    private final ActorRef letterTransportationActor;
    private final ActorRef mervillTransportationActor;

    @Autowired
    public TransportBrokerController(ActorRef letterTransportationActor, ActorRef mervillTransportationActor) {
        this.letterTransportationActor = letterTransportationActor;
        this.mervillTransportationActor = mervillTransportationActor;
    }
    @Operation(summary = "dispatch a list of new parcels")
    @PostMapping("/newParcels")
    public int newParcels(@Parameter(description = "a list of parcels to be dispatched") @RequestBody List<Parcel> parcels){
        for(Parcel parcel:parcels)
            newParcel(parcel);
        return 0;
    }

    @Operation(summary = "dispatch a single parcel and send a notification of new letters to receivers")
    @PostMapping("/newParcel")
    public int newParcel(@Parameter(description = "A parcel to be dispatched") @RequestBody Parcel parcel) {
        switch(parcel.getType()) {
            case 2: // Regular letters
            case 3: // Critical letters
                System.out.println("letterTransport");
                letterTransportationActor.tell(parcel, ActorRef.noSender());
                break;
            case 1: // Packages
                mervillTransportationActor.tell(parcel, ActorRef.noSender());
                break;
            default:
                // Handle unknown type
                return -1; // Indicate an error
        }
        return 0; // Indicate success
    }

}

