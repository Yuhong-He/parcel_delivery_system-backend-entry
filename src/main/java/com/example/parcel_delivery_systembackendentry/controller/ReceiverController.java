package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.message.MQ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/*
 * Every function needs to post a message to /log/newTrail*/
@RestController
@RequestMapping("/TransportationBroker")
public class ReceiverController {
    @GetMapping("/getHistory")
    public List<ParcelTrack> getHistory(@RequestParam int receiverID) {
        return null;
    }

    /*Define DeliverCallback callBack to customize how client deal with new notification
    run MQ.consumeNotification(callBack); in the SpringBoot Application*/

}
