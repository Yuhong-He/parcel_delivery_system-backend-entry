package com.example.broker.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.example.broker.entity.Parcel;
import org.springframework.beans.factory.annotation.Autowired;

public class LetterTransportationActor extends AbstractActor {
    private final ActorRef mervillTransportationActor;

    @Autowired
    public LetterTransportationActor(ActorRef mervillTransportationActor) {
        this.mervillTransportationActor = mervillTransportationActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Parcel.class, parcel -> {
                    if (parcel.getType() == 3) {
                        mervillTransportationActor.tell(parcel, getSelf());
                    }else {
                        sendToPostmanServer(parcel);
                    }
                })
                .build();
    }

    private void sendToPostmanServer(Parcel parcel) {
        System.out.println("send to postman server"+parcel.toString());
    }
}
