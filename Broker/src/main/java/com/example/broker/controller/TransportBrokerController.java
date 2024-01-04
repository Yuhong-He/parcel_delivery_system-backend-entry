package com.example.broker.controller;

import com.example.broker.entity.Parcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import akka.actor.ActorRef;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/")
public class TransportBrokerController {
    private final ActorRef letterTransportationActor;
    private final ActorRef mervilleTransportationActor;

    @Autowired
    public TransportBrokerController(ActorRef letterTransportationActor, ActorRef mervillTransportationActor) {
        this.letterTransportationActor = letterTransportationActor;
        this.mervilleTransportationActor = mervillTransportationActor;
    }

    @Operation(summary = "dispatch a single parcel and send a notification of new letters to receivers")
    @PostMapping("/distribute")
    public int newParcel(@Parameter(description = "A parcel to be dispatched") @RequestBody Parcel parcel) {
        switch(parcel.getType()) {
            case 2: // Regular letters
            case 3: // Critical letters
                letterTransportationActor.tell(parcel, ActorRef.noSender());
                break;
            case 1: // Packages
                mervilleTransportationActor.tell(parcel, ActorRef.noSender());
                break;
            default:
                return -1; // Indicate an error
        }
        return 0; // Indicate success
    }

}

