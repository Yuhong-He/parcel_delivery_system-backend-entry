package com.example.broker.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.example.broker.dto.ParcelTrack;
import com.example.broker.dto.Parcel;
import com.example.broker.message.MQ;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LetterTransportationActor extends AbstractActor {
    private final ActorRef mervilleTransportationActor;

    @Autowired
    public LetterTransportationActor(ActorRef mervillTransportationActor) {
        this.mervilleTransportationActor = mervillTransportationActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Parcel.class, parcel -> {
                    if (parcel.getType() == 3) {
                        mervilleTransportationActor.tell(parcel, getSelf());
                    }else {
                        updateParcelTrack(parcel);
                    }
                })
                .build();
    }

    private void updateParcelTrack(com.example.broker.dto.Parcel parcel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        Integer createBy = 1; // Not sure about User's ID
        ParcelTrack newTrack = new ParcelTrack("Broker Notify receiver", createBy, formattedDateTime);
        parcel.addTrack(newTrack);

        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            System.out.println("Exception during sending Parcel to MQ:" + e);
            e.printStackTrace();
        }
    }
}
