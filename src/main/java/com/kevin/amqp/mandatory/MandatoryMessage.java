package com.kevin.amqp.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：MandatoryMessage
 * @包名：com.kevin.mandatory
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/9/24 21:15
 * @版本：1.0
 * @描述：
 */
public class MandatoryMessage {

    private static final String MANDATORY_QUEUE_NAME = "mandatory_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 添加监听器，获取到没有被正确路由到合适队列的消息
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] ReplyCode '" + replyCode + "'\nreplyText '" + replyText + "'\nmessage '" + message + "'");
            }
        });

        String message = "hello world";
        channel.basicPublish("", MANDATORY_QUEUE_NAME, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        while (true) {
            Thread.sleep(5);
        }
    }
}
