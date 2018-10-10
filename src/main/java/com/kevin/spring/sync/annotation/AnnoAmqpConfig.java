package com.kevin.spring.sync.annotation;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @类名: AmqpConfig
 * @包名：com.kevin.spring.sync.annotation
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/9 20:31
 * @版本：1.0
 * @描述：
 */
@Configuration
public class AnnoAmqpConfig {

    private static final String DEFAULT_ROUTING_KEY = "spring.queue.sync";
    private static final String DEFAULT_QUEUE = DEFAULT_ROUTING_KEY;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("kevin");
        connectionFactory.setPassword("tttx");
        return connectionFactory;
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
}
