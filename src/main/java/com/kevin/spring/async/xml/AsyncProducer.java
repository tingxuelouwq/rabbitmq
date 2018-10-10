package com.kevin.spring.async.xml;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @类名: Producer
 * @包名：com.kevin.spring.async.xml
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/9 21:13
 * @版本：1.0
 * @描述：
 */
public class AsyncProducer {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring/async/spring-rabbitmq-producer.xml");
        AmqpTemplate amqpTemplate = (AmqpTemplate) context.getBean("rabbitTemplate");
        String message;
        for (int i = 0; i < 3; i++) {
            message = "test spring async=>" + i;
            amqpTemplate.convertAndSend(message);
            System.out.println(" [x] Sent '" + message + "'");
            Thread.sleep(1000);
        }
    }
}
