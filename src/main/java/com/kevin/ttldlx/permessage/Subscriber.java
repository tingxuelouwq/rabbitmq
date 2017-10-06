package com.kevin.ttldlx.permessage;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Subscriber {

    private static final String DEAD_LETTER_QUEUE_NAME = "dead_letter_queue";
    private static final String DEAD_LETTER_EXCHANGE_NAME = "dead_letter_exchange";
    private static final String DEAD_LETTER_ROUTING_KEY = "dead_letter";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(DEAD_LETTER_QUEUE_NAME, false, false, false, null);
        channel.exchangeDeclare(DEAD_LETTER_EXCHANGE_NAME, "direct");
        channel.queueBind(DEAD_LETTER_QUEUE_NAME, DEAD_LETTER_EXCHANGE_NAME, DEAD_LETTER_ROUTING_KEY);

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
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");            }
        };
        channel.basicConsume(DEAD_LETTER_QUEUE_NAME, true, consumer);
    }
}
