package com.kevin.amqp.pubsub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：EmitLog
 * @包名：com.kevin.pubsub
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/2 21:50
 * @版本：1.0
 * @描述：
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();

        /**
         * 交换器类型：Direct、Fanout、Topic、Header
         * Direct Exchange：处理路由键，需要将一个队列绑定到交换器上，要求该消息与一个特定的路由键完全匹配
         * Fanout Exchange：不处理路由键，只用简单的将队列绑定到交换机上，发送到该交换器的消息会被广播到所有与该交换器绑定的队列
         * Topic Exchange：将交换器和路由键绑定，路由键中的"#"匹配一个单词，"*"匹配零到多个单词
         */
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = "hello world";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
