package com.kevin.spring.async.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @类名: AsyncAnnoConsumer
 * @包名：com.kevin.spring.async.annotation
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/10 22:23
 * @版本：1.0
 * @描述：
 */
public class AsyncAnnoConsumer {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ConsumerConfig.class);
    }
}
