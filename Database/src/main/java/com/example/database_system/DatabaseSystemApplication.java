package com.example.database_system;

import com.alibaba.fastjson2.JSON;
import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.MongoDB.ParcelTrack;
import com.example.database_system.dto.ParcelTrackWithParcelID;
import com.example.database_system.message.MQ;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


@SecurityScheme(name = "Accesstoken", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
@SpringBootApplication
@ServletComponentScan
public class DatabaseSystemApplication {

    //use ParcelTrackWithParcelID to Save a track to a specific Parcel
    private static void newParcelTrack(@Autowired MongoTemplate mongoTemplate, ParcelTrackWithParcelID parcelTrackWithParcelID) {
        System.out.println("Parceltrack" + parcelTrackWithParcelID);
        Query query = new Query(Criteria.where("_id").is(parcelTrackWithParcelID.getParcelId()));
        Update update = new Update().push("tracks", new ParcelTrack(parcelTrackWithParcelID));
        mongoTemplate.updateFirst(query, update, Parcel.class);
        System.out.println("Parceltrack added successfully");
    }

    private static void newParcel(@Autowired MongoTemplate mongoTemplate, Parcel parcel) {
        System.out.println("Parcel" + parcel);
        mongoTemplate.save(parcel);
        System.out.println("Parcel added successfully");

    }

    public static void main(String[] args) {
        SpringApplication.run(DatabaseSystemApplication.class, args);

        new Thread(() -> {
            //MOM consumes Post requests
            try {
                MQ.consumePost((consumerTag, delivery) -> {
                    System.out.println("Received new post ");
                    Object message = JSON.parseObject(delivery.getBody(), Object.class);
                    if (message instanceof ParcelTrackWithParcelID)
                        newParcelTrack(null, (ParcelTrackWithParcelID) message);
                    else if (message instanceof Parcel)
                        newParcel(null, (Parcel) message);
                    else
                        System.out.println("Can't recognise the sent class: " + message.toString());

                });
            } catch (Exception e) {
                System.out.println("MQ exception:" + e);
                e.printStackTrace();
            }
        }).run();
    }

}
