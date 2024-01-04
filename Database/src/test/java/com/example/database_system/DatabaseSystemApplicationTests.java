package com.example.database_system;

import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.message.MQ;
import com.example.database_system.mybatis_service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class DatabaseSystemApplicationTests {

    @Autowired
    UserService userService;

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
            Parcel parcel = new Parcel();
            MQ.sendToDatabase(parcel);

        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }

    @Test
    void testUserService() {
        System.out.println(userService.getStudentById(4));
    }

}
