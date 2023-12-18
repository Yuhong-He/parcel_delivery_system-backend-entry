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
            System.out.println(message.toString());
        };

        try {
            MQ.consumeLog(callback);
            MQ.sendLog(new ParcelTrack("123", "Test Log", 1));
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }

}
