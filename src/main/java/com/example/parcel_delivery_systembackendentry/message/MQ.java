package com.example.parcel_delivery_systembackendentry.message;

import com.alibaba.fastjson2.JSON;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.rabbitmq.client.*;
import lombok.Value;

public class MQ {
    @Value("${global.MQServer}")
    public static final String address;

    public static final Channel establishConnection() throws Exception {
        System.out.println("Connecting to rabbitMQServer...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(address);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("Log", false, false, false, null);
        return channel;
    }

    public static final void sendLog(ParcelTrack parcelTrack) throws Exception {
        String message = JSON.toJSONString(parcelTrack);
        establishConnection().basicPublish("", "Log", null, message.getBytes());
        System.out.println("Sending " + message + " to rabbitMQServer...");
    }

    public static final void notifyReceiver(Object message1) throws Exception {
        String message = JSON.toJSONString(message1);
        Channel channel = establishConnection();
        channel.exchangeDeclare("ReceiverExchange", "direct");
        channel.basicPublish("ReceriberExchange", "",null,message.getBytes());
    }

    public static final void consumeNotification(int receiverId, DeliverCallback callBack) throws Exception {
        Channel channel = establishConnection();
        channel.exchangeDeclare("ReceiverExchange", "direct");
        channel.queueDeclare(String.valueOf(receiverId), false, false, false, null);
        channel.basicConsume("ReceiverExchange",true, callBack,consumerTag->{});
    }
}