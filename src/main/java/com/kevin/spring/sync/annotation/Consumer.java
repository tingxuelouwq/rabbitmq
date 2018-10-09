package com.kevin.spring.sync.annotation;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @类名: Consumer
 * @包名：com.kevin.spring.sync.annotation
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/9 20:40
 * @版本：1.0
 * @描述：
 */
public class Consumer {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AmqpConfig.class);
        AmqpTemplate amqpTemplate = context.getBean(AmqpTemplate.class);
        String message = amqpTemplate.receiveAndConvert().toString();
        System.out.println(" [x] Received '" + message + "'");
    }
}
