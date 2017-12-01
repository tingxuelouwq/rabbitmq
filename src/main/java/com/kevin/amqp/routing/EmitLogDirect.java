package com.kevin.amqp.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：EmitLogDirect
 * @包名：com.kevin.routing
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/4 9:18
 * @版本：1.0
 * @描述：
 */
public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String severity = getSeverity(args);
        String message = getMessage(args);

        channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] args) {
        if (args.length < 2) {
            return "hello world";
        }
        return joinString(args, " ", 1);
    }

    private static String joinString(String[] args, String delimiter, int startIndex) {
        int length = args.length;
        if (length == 0) {
            return "";
        }
        if (length < startIndex) {
            return "";
        }
        StringBuilder words = new StringBuilder(args[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(args[i]);
        }
        return words.toString();
    }

    private static String getSeverity(String[] args) {
        if (args.length < 1) {
            return "info";
        }
        return args[0];
    }
}
