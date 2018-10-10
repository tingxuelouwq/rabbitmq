package com.kevin.spring.async.xml;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @类名: Consumer
 * @包名：com.kevin.spring.async.xml
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/10 21:04
 * @版本：1.0
 * @描述：
 */
public class AsyncConsumer {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring/async/spring-rabbitmq-consumer.xml");
    }
}
