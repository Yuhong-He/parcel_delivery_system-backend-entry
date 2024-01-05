package com.example.broker.controller;

import akka.actor.ActorRef;
import com.example.broker.dto.Parcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TransportBrokerController {
    private final ActorRef letterTransportationActor;
    private final ActorRef mervilleTransportationActor;

    @Autowired
    public TransportBrokerController(ActorRef letterTransportationActor, ActorRef mervilleTransportationActor) {
        this.letterTransportationActor = letterTransportationActor;
        this.mervilleTransportationActor = mervilleTransportationActor;
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Broker System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Broker'>GitHub page</a>.</h2>";
    }

    @Operation(summary = "dispatch a single parcel and send a notification of new letters to receivers")
    @PostMapping("/distribute")
    public int newParcel(@Parameter(description = "A parcel to be dispatched") @RequestBody Parcel parcel) {
        switch(parcel.getType()) {
            case 1: // Packages
                mervilleTransportationActor.tell(parcel, ActorRef.noSender());
                break;
            case 2: // Regular letters
            case 3: // Critical letters
                letterTransportationActor.tell(parcel, ActorRef.noSender());
                break;
            default:
                return -1; // Indicate an error
        }
        return 0; // Indicate success
    }

}

