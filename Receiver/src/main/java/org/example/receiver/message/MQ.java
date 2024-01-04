package org.example.receiver.message;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.example.receiver.entity.Parcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQ {
    private static String address = "amqps://ucd_parcel_admin:BylaSoDu7byPasF2@b-2cb6f3fb-3957-4aa5-ad43-ca2b22178e62.mq.eu-west-1.amazonaws.com:5671/ucd_parcel";
    private static Boolean durable = false;
    private static Boolean autoAck = true;

    private static  Connection connection;
    @Autowired
    private void setStaticFields(
            @Value("${MQ.address}") String s,
            @Value("#{new Boolean('${MQ.durable}')}") Boolean b1,
            @Value("#{new Boolean('${MQ.autoAck}')}") Boolean b2) {
        address = s;
        durable = b1;
        autoAck = b2;
    }

    public static void sendToDatabase(Parcel parcel) throws Exception {
        String message = JSON.toJSONString(parcel);
        establishConnection().basicPublish("", "Database", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("Sending Log: " + message + " to Log System...");
        connection.close();
    }

    public static Channel establishConnection() throws Exception {
        System.out.println("Connecting to rabbitMQServer:" + address + " ...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(address);
        connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("Database", durable, false, false, null);
        return channel;
    }

}
