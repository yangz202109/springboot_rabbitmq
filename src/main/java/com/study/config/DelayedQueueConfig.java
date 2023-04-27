package com.study.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/5/27 - 16:30
 * 创建延迟交换机和队列,并绑定
 */
@Configuration
public class DelayedQueueConfig {
    /*交换机名称*/
    public static final String DELAYED_EXCHANGE_NAME = "DE";
    /*队列名称*/
    public static final String DELAYED_QUEUE_NAME = "DQN";
    /*路由key*/
    public static final String DELAYED_ROUTING_KEY = "DK";

    /*创建交换机*/
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");

        /*
         * 创建自定义交换机
         * String name ： 交换机名称
         * String type  ：交换机类型
         * boolean durable ：是否持久化
         * boolean autoDelete ：是否自动删除
         * arguments : 其他参数
         * */

        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    /*创建队列*/
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME, false);
    }

    /*绑定*/
    @Bean
    public Binding delayedQueueBindDelayedExchange() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY).noargs();
    }
}
