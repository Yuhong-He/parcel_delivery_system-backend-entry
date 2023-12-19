package com.example.parcel_delivery_systembackendentry;

import com.alibaba.fastjson2.JSON;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.message.MQ;
import com.rabbitmq.client.DeliverCallback;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParcelDeliverySystemBackendEntryApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void MQTest() {
        DeliverCallback callback = (consumerTag, delivery) -> {
            ParcelTrack message = JSON.parseObject(delivery.getBody(), ParcelTrack.class);
            System.out.println("Received: :"+ message.toString());
        };

        try {
            ParcelTrack parcelTrack1 = new ParcelTrack("123", "Test Log", 1);
            ParcelTrack parcelTrack2 = new ParcelTrack("456", "Test Log", 1);
            MQ.consumeLog(callback);
            MQ.sendLog(parcelTrack1);
            MQ.consumeNotification(1, callback);
            MQ.consumeNotification(2, callback);
            MQ.notifyReceiver(parcelTrack1, 1);
            MQ.notifyReceiver(parcelTrack2, 2);
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }

}
