package com.example.broker.actors;

import akka.actor.AbstractActor;
import com.example.broker.dto.ParcelTrack;
import com.example.broker.dto.Parcel;
import com.example.broker.message.MQ;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class MervilleTransportationActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Parcel.class, parcel -> {
                    System.out.println("I am in MervilleTransportationActor");
                    if (parcel.getType() == 1 || parcel.getType() == 3) {
                        updateParcelTrack(parcel);
                    }
                })
                .build();
    }


    private void updateParcelTrack(Parcel parcel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        Integer createBy = -1;
        ParcelTrack newTrack = new ParcelTrack("Broker send parcel to Merville Room", createBy, formattedDateTime);
        parcel.addTrack(newTrack);

        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            log.error("Exception during sending Parcel to MQ:" + e);
            e.printStackTrace();
        }
    }
}

