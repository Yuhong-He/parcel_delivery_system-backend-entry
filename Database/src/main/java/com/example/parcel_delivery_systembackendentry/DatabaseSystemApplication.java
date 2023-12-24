package com.example.parcel_delivery_systembackendentry;

import com.alibaba.fastjson2.JSON;
import com.example.parcel_delivery_systembackendentry.MongoDB.Parcel;
import com.example.parcel_delivery_systembackendentry.MongoDB.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.dto.ParcelTrackWithParcelID;
import com.example.parcel_delivery_systembackendentry.enumeration.message.MQ;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.Resource;
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
    @Resource
//    private static ParcelRepository parcelRepository;
    private static MongoTemplate mongoTemplate;

    //use ParcelTrackWithParcelID to Save a track to a specific Parcel
    private static void newParcelTrack(ParcelTrackWithParcelID parcelTrackWithParcelID) {
        Query query = new Query(Criteria.where("_id").is(parcelTrackWithParcelID.getParcelId()));
        Update update = new Update().push("tracks", new ParcelTrack(parcelTrackWithParcelID));
        mongoTemplate.updateFirst(query, update, Parcel.class);
    }

    private static void newParcel(Parcel parcel) {
        mongoTemplate.save(parcel);
    }

    public static void main(String[] args) {

        new Thread(() -> {
            //MOM consumes Post requests
            try {
                MQ.consumePost((consumerTag, delivery) -> {

                    Object message = JSON.parseObject(delivery.getBody(), Object.class);
                    if (message instanceof ParcelTrackWithParcelID)
                        newParcelTrack((ParcelTrackWithParcelID) message);
                    else if (message instanceof Parcel)
                        newParcel((Parcel) message);

                });
            } catch (Exception e) {
                System.out.println("MQ exception:" + e);
                e.printStackTrace();
            }
        }).run();

        SpringApplication.run(DatabaseSystemApplication.class, args);
    }

}
