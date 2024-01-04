package com.example.broker.actors;

import akka.actor.AbstractActor;
import com.example.broker.dto.Parcel;
import com.example.broker.dto.ParcelTrack;
import com.example.broker.message.MQ;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class MervilleTransportationActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Parcel.class, parcel -> {
                    if (parcel.getType() == 1 || parcel.getType() == 3) {
                        updateParcelTrack(parcel);
                    }
                })
                .build();
    }


    private void updateParcelTrack(Parcel parcel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        ParcelTrack newTrack = new ParcelTrack("Broker send parcel to Merville Room", -1, true, -1, formattedDateTime);
        parcel.setTracks(List.of(newTrack));

        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            log.error("Exception during sending Parcel to MQ:" + e);
        }
    }
}

