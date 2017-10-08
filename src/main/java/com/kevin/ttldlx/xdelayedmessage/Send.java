package com.kevin.ttldlx.xdelayedmessage;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @类名：Send
 * @包名：com.kevin.ttldlx.xdelayedmessage
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/8 19:58
 * @版本：1.0
 * @描述：
 */
public class Send {

    private static final String EXCHANGE_NAME = "delayed_exchange";
    private static final String ROUTING_KEY = "delayed_routing_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        channel.exchangeDeclare(EXCHANGE_NAME, "x-delayed-message", false, false, arguments);

        String message = "hello world";

        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", 5000);
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .headers(headers)
                .build();
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, props, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
