package com.kevin.amqp.ttldlx.perqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Publisher {

    private static final String DELAY_QUEUE_NAME = "delay_queue";
    private static final String DEAD_LETTER_EXCHANGE_NAME = "dead_letter_exchange";
    private static final String DEAD_LETTER_ROUTING_KEY = "dead_letter";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        arguments.put("x-message-ttl", 3000);
        channel.queueDeclare(DELAY_QUEUE_NAME, false, false, false, arguments);

        String message = getMessage(args);
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
