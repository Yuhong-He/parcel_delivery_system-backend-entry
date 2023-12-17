package com.example.parcel_delivery_systembackendentry.message;
import com.alibaba.fastjson2.JSON;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import lombok.Value;

import static jdk.internal.org.jline.utils.Colors.s;

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

    public static final void sendLog(ParcelTrack parcelTrack) throws Exception{
        String  message = JSON.toJSONString(parcelTrack);
        establishConnection().basicPublish("", "Log", null,message.getBytes());
        System.out.println("Sending "+message+" to rabbitMQServer...");
    }
    public static final void notifyReceiver(String receiver, Parcel parcel)throws Exception{
        Channel channel = establishConnection();
        channel.exchangeDeclare("ReceiverExchange","direct");
        channel.basicPublish("ReceiverExchange","");
    }
}
