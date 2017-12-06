package com.kevin.spring.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @class: Producer
 * @package: com.kevin.spring.producer
 * @author: kevin[wangqi2017@xinhua.org]
 * @date: 2017/12/4 20:25
 * @version: 1.0
 * @desc:
 */
public class Producer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        AmqpTemplate amqpTemplate = context.getBean(RabbitTemplate.class);
        String message = "test spring sync message";
        amqpTemplate.convertAndSend(message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
