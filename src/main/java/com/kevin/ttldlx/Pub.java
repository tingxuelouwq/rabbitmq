package com.kevin.ttldlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Pub {

    private static final String DELAY_QUEUE_NAME = "delay_queue";
    private static final String DEAD_LETTER_QUEUE_NAME = "dead_letter_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        Map<String, Object> props = new HashMap<>();
        // set the dead-letter exchange to the default exchange
        props.put("x-dead-letter-exchange", "");
        // when the message expires, change the routing key into the destination queue name
        props.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_NAME);
        // the time in milliseconds to keep the message in the queue
        props.put("x-message-ttl", 3000);

        channel.queueDeclare(DELAY_QUEUE_NAME, false, false, false, props);

        String message = getMessage(args);
         // publish to the default exchange with the the delayed queue name as routing key
        channel.basicPublish("", DELAY_QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings){
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0 ) return "";
        if (length < startIndex ) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
