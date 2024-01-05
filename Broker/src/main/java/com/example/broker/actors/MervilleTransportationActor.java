package com.example.broker.actors;

import akka.actor.AbstractActor;
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

@Slf4j
public class MervilleTransportationActor extends AbstractActor {

    RestTemplate restTemplate = new RestTemplate();

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
            String studentEmail = restTemplate.getForObject("http://localhost:18087/user/getUserEmail?id=" + parcel.getStudent(), String.class);
            Email email = new Email(studentEmail, "New Parcel to Merville Room",
                    getCollectParcelEmailBody(parcel.getType()));
            restTemplate.postForEntity("https://mail.ucdparcel.ie/send", EmailEncryptor.encrypt(email), String.class);
        } catch (Exception e) {
            log.error("Exception during sending Parcel to MQ:" + e);
        }
    }

    private String getCollectParcelEmailBody(int type) {
        String parcelType = switch (type) {
            case 1 -> "Package";
            case 2 -> "Regular Mail";
            case 3 -> "Critical Mail";
            default -> "Unknown type";
        };
        return "<p>Dear student,</p>" +
                "<p>You have got a " + parcelType +  " send to Merville room, please come to collect.</p>";
    }
}

