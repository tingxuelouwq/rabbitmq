package com.kevin.spring.async.annotation;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @类名: ConsumerConfig
 * @包名：com.kevin.spring.async.annotation
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/10 22:18
 * @版本：1.0
 * @描述：
 */
@Configuration
public class ConsumerConfig {

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
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(DEFAULT_ROUTING_KEY);
        template.setQueue(DEFAULT_QUEUE);
        return template;
    }

    @Bean
    public Queue defaultQueue() {
        return new Queue(DEFAULT_QUEUE, false);
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer() {
        SimpleMessageListenerContainer listenerContainer =
                new SimpleMessageListenerContainer(connectionFactory());
        listenerContainer.setQueues(defaultQueue());
        listenerContainer.setMessageListener(new MessageListenerAdapter(
                new RecvMsgHandle(), "onMessage"
        ));
        return listenerContainer;
    }
}
