package com.kevin.exclusive;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：ExclusiveQueue
 * @包名：com.kevin.exclusive
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/2 20:58
 * @版本：1.0
 * @描述：exclusive为true时，有以下3个特征：
 * 1、队列变为私有，即只有你的应用程序才能够消费队列消息。
 * 2、只对首次声明它的连接可见，会在其连接断开时自动删除。
 */
public class ExclusiveQueue {

    private static final String EXCLUSIVE_QUEUE_NAME = "exclusive_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(EXCLUSIVE_QUEUE_NAME, true, true, false, null);
        System.out.println(declareOk.getQueue());

        String message = "hello world";
        channel.basicPublish("", EXCLUSIVE_QUEUE_NAME, null,message.getBytes("UTF-8"));

        // close the channel, check if the queue is deleted
        System.out.println("Try to close channel");
        channel.close();
        System.out.println("Channel closed");

        System.out.println("Create a new channel");
        Channel channel2 =connection.createChannel();
        AMQP.Queue.DeclareOk declareOk2 = channel2.queueDeclarePassive(EXCLUSIVE_QUEUE_NAME);

        // we can access the exclusive queue from another channel
        System.out.println(declareOk2.getQueue());

        String message2 = "hello rabbitmq";
        channel2.basicPublish("", EXCLUSIVE_QUEUE_NAME, null, message2.getBytes("UTF-8"));
        System.out.println("Message published through the new channel");

        System.out.println("Try to close Connection");
        connection.close();
        System.out.println("Connection closed");
    }
}
