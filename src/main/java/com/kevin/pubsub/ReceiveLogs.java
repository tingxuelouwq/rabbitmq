package com.kevin.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：ReceiveLogs
 * @包名：com.kevin.pubsub
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/2 21:55
 * @版本：1.0
 * @描述：
 */
public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();

        /**
         * 这里我们使用了临时队列，理由如下；
         * 1、我们需要监听到所有的日志信息，而非其子集；
         * 2、我们只对即时的信息流感兴趣，而非旧的日志信息。
         * 因此，我们需要一个随机队列，并且在消费者断开时，该队列可以自动被删除。
         * 不带参的channel.queueDeclare()会创建一个非持久的，排他的，自动删除的随机队列，这正好满足我们的需求
         */
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

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
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
