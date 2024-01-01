package com.example.postman;

import com.example.postman.dto.ParcelTrackWithParcelID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class PostmanApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void MQTest() {

        try {
            String desc = "Test";
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            ParcelTrackWithParcelID parcel = new ParcelTrackWithParcelID("014da20d-867a-4c2c-a1a8-10b9360abce1", desc, 3, false, 0, formattedDateTime);
//            MQ.sendToDatabase(parcel);

        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }

}
