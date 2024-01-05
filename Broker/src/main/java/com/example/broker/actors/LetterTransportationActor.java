package com.example.broker.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.alibaba.fastjson2.JSON;
import com.example.broker.dto.Email;
import com.example.broker.dto.Parcel;
import com.example.broker.dto.ParcelTrack;
import com.example.broker.message.MQ;
import com.example.broker.utils.EmailEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
public class LetterTransportationActor extends AbstractActor {

    RestTemplate restTemplate = new RestTemplate();

    private final ActorRef mervilleTransportationActor;

    public LetterTransportationActor(ActorRef mervilleTransportationActor) {
        this.mervilleTransportationActor = mervilleTransportationActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Parcel.class, parcel -> {
                    if (parcel.getType() == 3) {
                        mervilleTransportationActor.tell(parcel, getSelf());
                    } else {
                        updateParcelTrack(parcel);
                    }
                })
                .build();
    }

    private void updateParcelTrack(com.example.broker.dto.Parcel parcel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        String postmanIdsStr = restTemplate.getForObject("http://localhost:18087/user/getPostmanIds", String.class);
        List<Integer> postmanIds = JSON.parseArray(postmanIdsStr, Integer.class);
        int randomPostmanId;
        if (postmanIds != null && !postmanIds.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(postmanIds.size());
            randomPostmanId = postmanIds.get(randomIndex);
        } else {
            log.error("No postman found");
            return;
        }

        ParcelTrack newTrack = new ParcelTrack("Broker notify receiver", randomPostmanId, false, -1, formattedDateTime);
        parcel.setTracks(List.of(newTrack));

        try {
            MQ.sendToDatabase(parcel);
            String studentEmail = restTemplate.getForObject("http://localhost:18087/user/getUserEmail?id=" + parcel.getStudent(), String.class);
            Email email = new Email(studentEmail, "Confirm Delivery Address",
                    getConfirmAddressEmailBody());
            restTemplate.postForEntity("https://mail.ucdparcel.ie/send", EmailEncryptor.encrypt(email), String.class);
        } catch (Exception e) {
            log.error("Exception during sending Parcel to MQ:" + e);
        }
    }

    private String getConfirmAddressEmailBody() {
        return "<p>Dear student,</p>" +
                "<p>You have a new mail about to be sent, please log in to the UCD Parcel Delivery System to confirm the delivery address.</p>";
    }
}
