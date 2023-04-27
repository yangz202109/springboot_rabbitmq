package com.study.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/5/24 - 14:24
 * 配置类：创建交换机和队列,并进行绑定
 */
@Configuration
public class RabbitMQConfig {

    /*普通交换机名称*/
    @Value("${rabbitmq.exchange.normal}")
    private String NORMAL_EXCHANGE;
    /*死信交换机名称*/
    @Value("${rabbitmq.exchange.dead}")
    private String DEAD_EXCHANGE;

    /*普通队列名称*/
    @Value("${rabbitmq.queue.queue1}")
    private String NORMAL_QUEUE1;
    @Value("${rabbitmq.queue.queue2}")
    private String NORMAL_QUEUE2;
    @Value("${rabbitmq.queue.queue3}")
    private String NORMAL_QUEUE3;

    /*死信队列名称*/
    @Value("${rabbitmq.queue.dead_queue}")
    private String DEAD_QUEUE;


    /*创建普通交换机*/
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    /*创建死信交换机*/
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    /*----------------------------------------------------------------------------*/

    /*创建普通队列A-过期时间10秒*/
    @Bean
    public Queue normalQueueA() {
        /*设置关联的死信交换机*/
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 10000); //该队列存放消息的时间ms
        return new Queue(NORMAL_QUEUE1, false, false, false, arguments);
    }

    /*A队列绑定交换机*/
    @Bean
    public Binding queueABindX() {
        return BindingBuilder.bind(normalQueueA()).to(normalExchange()).with("XA");
    }

    /*创建普通队列B-过期时间40秒*/
    @Bean
    public Queue normalQueueB() {
        /*设置关联的死信交换机*/
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 40000); //该队列存放消息的时间ms
        return new Queue(NORMAL_QUEUE2, false, false, false, arguments);
    }

    /*B队列绑定交换机*/
    @Bean
    public Binding queueBBindX() {
        return BindingBuilder.bind(normalQueueB()).to(normalExchange()).with("XB");
    }

    /*创建普通队列C*/
    @Bean
    public Queue normalQueueC(){
        /*设置关联的死信交换机*/
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        //不应该在队列中设置过期时间
        return new Queue(NORMAL_QUEUE3, false, false, false, arguments);
    }

    /*c队列绑定交换机*/
    @Bean
    public Binding queueCBindX(){
        return BindingBuilder.bind(normalQueueC()).to(normalExchange()).with("XC");
    }

    /*创建死信队列*/
    @Bean
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE);
    }

    /*死信队列绑定死信交换机*/
    @Bean
    public Binding deadQueueBind() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("YD");
    }


}
