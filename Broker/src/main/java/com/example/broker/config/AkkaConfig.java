package com.example.broker.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.broker.actors.MervilleTransportationActor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.broker.actors.LetterTransportationActor;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("parcelSystem");
    }

    @Bean
    public ActorRef letterTransportationActor(ActorSystem actorSystem) {
        ActorRef mervilleTransportationActor = mervilleTransportationActor(actorSystem);
        return actorSystem.actorOf(Props.create(LetterTransportationActor.class, mervilleTransportationActor), "letterTransportationActor");
    }

    @Bean
    public ActorRef mervilleTransportationActor(ActorSystem actorSystem) {
        return actorSystem.actorOf(Props.create(MervilleTransportationActor.class), "mervilleTransportationActor");
    }
}
