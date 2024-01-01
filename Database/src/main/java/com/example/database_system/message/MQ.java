package com.example.database_system.message;

import com.alibaba.fastjson2.JSON;
import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.MongoDB.ParcelTrack;
import com.example.database_system.dto.ParcelTrackWithParcelID;
import com.rabbitmq.client.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MQ implements ApplicationRunner {
    private static String address = "amqps://ucd_parcel_admin:BylaSoDu7byPasF2@b-2cb6f3fb-3957-4aa5-ad43-ca2b22178e62.mq.eu-west-1.amazonaws.com:5671/ucd_parcel";
    private static Boolean durable = false;
    private static Boolean autoAck = true;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private void setStaticFields(
            @Value("${MQ.address}") String s,
            @Value("#{new Boolean('${MQ.durable}')}") Boolean b1,
            @Value("#{new Boolean('${MQ.autoAck}')}") Boolean b2) {
        address = s;
        durable = b1;
        autoAck = b2;
    }

    public static void sendToDatabase(ParcelTrackWithParcelID parcelTrackWithParcelID) throws Exception {
        String message = JSON.toJSONString(parcelTrackWithParcelID);
        establishConnection().basicPublish("", "Database", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("Sending Log: " + message + " to Log System...");
    }

    public static void consumePost(DeliverCallback callBack) throws Exception {
        Channel channel = establishConnection();
        channel.basicConsume("Database", autoAck, callBack, consumerTag -> {
        });
    }

    public static Channel establishConnection() throws Exception {
        System.out.println("Connecting to rabbitMQServer:" + address + " ...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(address);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("Database", durable, false, false, null);
        return channel;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            // MOM consumes Post requests
            try {
                MQ.consumePost((consumerTag, delivery) -> {
                    System.out.println("Received new post ");
                    Parcel message = JSON.parseObject(delivery.getBody(), Parcel.class);
                    newParcelTrack(message);
                });
            } catch (Exception e) {
                System.out.println("MQ exception:" + e);
                e.printStackTrace();
            }
        }).run();

    }

    private void newParcelTrack(Parcel parcel) {
        for (ParcelTrack parcelTrack : parcel.getTracks()) {
            System.out.println("Adding Parceltrack" + parcelTrack);
        }
        Query query = new Query(Criteria.where("_id").is(parcel.getId()));
        Update update = new Update().push("tracks", parcel.getTracks());
        mongoTemplate.updateFirst(query, update, Parcel.class);
        System.out.println("Parceltrack added successfully");
    }

}
