package com.kevin.amqp.publishconfirm;

import com.kevin.amqp.util.IdGeneratorUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @类名：ConfirmDontLoseMessages
 * @包名：com.kevin.publishconfirm
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/9/21 22:17
 * @版本：1.0
 * @描述：
 */
public class ConfirmDontLoseMessages {

    private static int msgCount = 10000;
    private static final String QUEUE_NAME = "confirm-test";
    private static ConnectionFactory factory;

    public static void main(String[] args) {
        if (args.length > 0) {
            msgCount = Integer.parseInt(args[0]);
        }

        factory = new ConnectionFactory();

        // Consume msgCount messages.
        (new Thread(new Consumer())).start();
        // Publish msgCount messages and wait for confirms.
        (new Thread(new Publisher())).start();
    }

    private static class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                // Setup
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                // Consume
                DefaultConsumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body)
                            throws IOException
                    {
                        System.out.println(IdGeneratorUtils.next());
                    }
                };
                channel.basicConsume(QUEUE_NAME, true, consumer);
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Publisher implements Runnable {
        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();

                // Setup
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                channel.confirmSelect();

                // Publish
                for (long i = 0; i < msgCount; i++) {
                    channel.basicPublish("", QUEUE_NAME,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            "nop".getBytes("UTF-8"));
                }
                channel.waitForConfirmsOrDie();

                // Cleanup
                channel.close();
                connection.close();

                long endTime = System.currentTimeMillis();
                System.out.printf("Test took %.3fs\n",
                        (float)(endTime - startTime)/1000);
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
