package com.kevin.amqp.ttldlx.xdelayedmessage;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：Recv
 * @包名：com.kevin.ttldlx.xdelayedmessage
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/8 20:38
 * @版本：1.0
 * @描述：
 */
public class Recv {

    private static final String EXCHANGE_NAME = "delayed_exchange";
    private static final String QUEUE_NAME = "delayed_queue";
    private static final String ROUTING_KEY = "delayed_routing_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException
            {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'" +
                        "\nx-delay: " + properties.getHeaders().get("x-delay"));
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
