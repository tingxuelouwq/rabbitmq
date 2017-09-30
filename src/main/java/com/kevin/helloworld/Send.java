package com.kevin.helloworld;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名: Send
 * @包名：com.kevin.helloworld
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/9/19 9:58
 * @版本：1.0
 * @描述：生产者代码
 */
public class Send {

    /**
     * 消息队列名称
     */
    private static final String QUEUE_NAME = "helloworld";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        factory.setPort(AMQP.PROTOCOL.PORT);
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建频道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 创建消息
        String message = "hello world";
        // 发送消息
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
