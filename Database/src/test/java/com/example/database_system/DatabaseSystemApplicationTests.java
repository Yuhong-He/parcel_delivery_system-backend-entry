package com.example.database_system;

import com.example.database_system.MongoDB.Parcel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class DatabaseSystemApplicationTests {
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
//            MQ.sendToDatabase(parcel);

        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }

//    @Test
//    void MongoDBTest(@Autowired ParcelRepository parcelRepository) {
//        Slice<Parcel> parcelSlice = parcelRepository.findAllByStudent(4, PageRequest.of(0, 3));
//        Slice<Parcel> letterSlice = parcelRepository.findAllByType(3, PageRequest.of(0, 3));
//        while (true) {
//            parcelSlice.getContent().forEach((e) -> {
//                System.out.println(e);
//            });
//            if (parcelSlice.hasNext())
//                parcelSlice = parcelRepository.findAllByStudent(4, parcelSlice.nextPageable());
//            else
//                break;
//        }
//        while (true) {
//            letterSlice.getContent().forEach((e) -> {
//                System.out.println(e);
//            });
//            if (letterSlice.hasNext())
//                letterSlice = parcelRepository.findAllByType(3, letterSlice.nextPageable());
//            else
//                break;
//        }

//    }

}
