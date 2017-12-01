package com.kevin.amqp.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：NewTask
 * @包名：com.kevin.workqueues
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/9/21 21:35
 * @版本：1.0
 * @描述：
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        
        String message = getMessage(args);

        channel.basicPublish("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] args) {
        if (args.length < 1) {
            return "Hello world!";
        }

        return joinString(args, " ");
    }

    private static String joinString(String[] args, String delimiter) {
        int length = args.length;
        if (length == 0) {
            return "";
        }

        StringBuilder words = new StringBuilder(args[0]);
        for (int i = 0; i < length; i++) {
            words.append(delimiter).append(args[i]);
        }
        return words.toString();
    }
}
