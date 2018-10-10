package com.kevin.spring.async.annotation;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @类名: ProducerConfig
 * @包名：com.kevin.spring.async.annotation
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/10 22:02
 * @版本：1.0
 * @描述：
 */
@Configuration
public class ProducerConfig {

    private static final String DEFAULT_ROUTING_KEY = "spring.queue.async";
    private static final String DEFAULT_QUEUE = DEFAULT_ROUTING_KEY;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory =
                new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername("kevin");
        cachingConnectionFactory.setPassword("tttx");
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue defaultQueue() {
        return new Queue(DEFAULT_QUEUE, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(DEFAULT_ROUTING_KEY);
        template.setQueue(DEFAULT_QUEUE);
        return template;
    }

    @Bean
    public ScheduledProducer scheduledProducer() {
        return new ScheduledProducer();
    }

    @Bean
    public BeanPostProcessor postProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }

    private class ScheduledProducer {
        @Autowired
        private RabbitTemplate template;

        private AtomicInteger counter = new AtomicInteger();

        @Scheduled(fixedRate = 3000)
        public void sendMessage() {
            String message = "test spring sync message =>" + counter.incrementAndGet();
            template.convertAndSend(message);
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
