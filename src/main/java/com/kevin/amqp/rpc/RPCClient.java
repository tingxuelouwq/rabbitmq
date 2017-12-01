package com.kevin.amqp.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @类名：RPCClient
 * @包名：com.kevin.rpc
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/10/5 19:26
 * @版本：1.0
 * @描述：
 */
public class RPCClient {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kevin");
        factory.setPassword("tttx");
        Connection connection = null;

        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String replyQueueName = channel.queueDeclare().getQueue();
            String message = getMessage(args);
            System.out.println(" [x] Requesting fib(" + message + ")");

            String correlationId = UUID.randomUUID().toString();
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlationId)
                    .replyTo(replyQueueName)
                    .build();
            channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes("UTF-8"));

            BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    if (properties.getCorrelationId().equals(correlationId)) {
                        blockingQueue.offer(new String(body, "UTF-8"));
                    }
                }
            };
            channel.basicConsume(replyQueueName, true, consumer);

            String response = blockingQueue.take();
            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getMessage(String[] strings){
        if (strings.length < 1)
            return "3";
        return strings[0];
    }
}
