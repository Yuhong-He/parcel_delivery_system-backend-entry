package com.example.broker.actors;

import akka.actor.AbstractActor;
import com.example.broker.entity.Parcel;

public class MervillTransportationActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Parcel.class, parcel -> {
                    if (parcel.getType() == 1 || parcel.getType() == 3) {
                        sendToMervillRoom(parcel);
                    }
                })
                .build();
    }

    private void sendToMervillRoom(Parcel parcel) {
        System.out.println("send to mervill room:"+parcel.toString());
    }
}

