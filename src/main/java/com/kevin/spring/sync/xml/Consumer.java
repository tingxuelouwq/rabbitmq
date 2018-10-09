package com.kevin.spring.sync.xml;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @class: Consumer
 * @package: com.kevin.spring.consumer
 * @author: kevin[wangqi2017@xinhua.org]
 * @date: 2017/12/4 20:53
 * @version: 1.0
 * @desc:
 */
public class Consumer {

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring/sync/spring-context.xml");
        AmqpTemplate amqpTemplate = context.getBean(RabbitTemplate.class);
        String message = amqpTemplate.receiveAndConvert().toString();
        System.out.println(" [x] Received '" + message + "'");
    }
}
