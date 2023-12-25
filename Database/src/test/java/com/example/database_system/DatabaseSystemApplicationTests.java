package com.example.database_system;

import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.MongoDB.ParcelRepository;
import com.example.database_system.MongoDB.ParcelTrack;
import com.example.database_system.dto.ParcelInfo;
import com.example.database_system.message.MQ;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
class DatabaseSystemApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void MQTest() {
//        DeliverCallback callback = (consumerTag, delivery) -> {
//            ParcelTrack message = JSON.parseObject(delivery.getBody(), ParcelTrack.class);
//            System.out.println("Received: :"+ message.toString());
//        };

        try {
            ParcelInfo parcelInfo = new ParcelInfo();
            String desc = "Estate Service created parcel label";
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            ParcelTrack parcelTrack = new ParcelTrack(desc, 3, formattedDateTime);
            Parcel parcel = new Parcel(parcelInfo, List.of(parcelTrack));
            MQ.sendToDatabase(parcel);

        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }

    @Test
    void MongoDBTest(@Autowired ParcelRepository parcelRepository) {
        Slice<Parcel> parcelSlice = parcelRepository.findAllByStudent(4, PageRequest.of(0, 3));
        while (parcelSlice.hasNext()) {
            parcelSlice.getContent().forEach((e) -> {
                System.out.println(e);
            });
            parcelSlice = parcelRepository.findAllByStudent(4, parcelSlice.nextPageable());
        }
    }

}
