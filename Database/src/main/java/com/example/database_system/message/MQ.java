package com.example.database_system.message;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQ {

    private static String address = "amqps://ucd_parcel_admin:BylaSoDu7byPasF2@b-2cb6f3fb-3957-4aa5-ad43-ca2b22178e62.mq.eu-west-1.amazonaws.com:5671/ucd_parcel";
    private static Boolean durable = false;
    private static Boolean autoAck = true;

    @Autowired
    private void setStaticFields(
            @Value("${MQ.address}") String s,
            @Value("#{new Boolean('${MQ.durable}')}") Boolean b1,
            @Value("#{new Boolean('${MQ.autoAck}')}") Boolean b2){
        address = s;
        durable = b1;
        autoAck = b2;
    }

    public static Channel establishConnection() throws Exception {
        System.out.println("Connecting to rabbitMQServer:"+address+" ...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(address);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("Database", durable, false, false, null);
        return channel;
    }

    public static void sendToDatabase(Object object) throws Exception {
        String message = JSON.toJSONString(object);
        establishConnection().basicPublish("", "Database", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("Sending Log: " + message + " to Log System...");
    }

    public static void consumePost(DeliverCallback callBack) throws Exception {
        Channel channel = establishConnection();
        channel.basicConsume("Database", autoAck, callBack, consumerTag -> {
        });
    }
//    public static void notifyReceiver(ParcelTrack parcelTrack, int receiverID) throws Exception {
//        String message = JSON.toJSONString(parcelTrack);
//        Channel channel = establishConnection();
//        channel.exchangeDeclare("ReceiverExchange", "direct");
//        channel.basicPublish("ReceiverExchange", String.valueOf(receiverID), MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
//        System.out.println("Sending notification: " + message + " to Receiver " + " ...");
//    }
//
//    public static void consumeNotification(int receiverId, DeliverCallback callBack) throws Exception {
//        Channel channel = establishConnection();
//        channel.exchangeDeclare("ReceiverExchange", "direct");
//        channel.queueDeclare(String.valueOf(receiverId), durable,true, false, null);
//        channel.queueBind(String.valueOf(receiverId),"ReceiverExchange",String.valueOf(receiverId));
//        System.out.println("Binding "+receiverId+" to Receiver exchange...");
//        channel.basicConsume(String.valueOf(receiverId), autoAck, callBack, consumerTag -> {
//        });
//
//    }


}
