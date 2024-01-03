package com.example.broker.actors;

import akka.actor.AbstractActor;
import com.example.broker.dto.ParcelTrack;
import com.example.broker.dto.Parcel;
import com.example.broker.message.MQ;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MervillTransportationActor extends AbstractActor {

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
        Integer createBy = 1; // Not sure about User's ID
        ParcelTrack newTrack = new ParcelTrack("Broker Give Parcel to Mervill", createBy, formattedDateTime);
        parcel.addTrack(newTrack);

        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            System.out.println("Exception during sending Parcel to MQ:" + e);
            e.printStackTrace();
        }
    }
}

