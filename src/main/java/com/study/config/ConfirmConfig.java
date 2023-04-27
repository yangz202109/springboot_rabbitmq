package com.study.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/5/28 - 10:02
 * 发布确认
 */
@Configuration
public class ConfirmConfig {
    /*交换机*/
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    /*队列*/
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    /*路由key*/
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";

    /*备份交换机*/
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    /*备份队列*/
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    /*报警队列*/
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    /*创建交换机*/
    @Bean
    public DirectExchange confirmExchange() {
        /*设置确认交换机的备份交换机*/
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange",BACKUP_EXCHANGE_NAME);
        return new DirectExchange(CONFIRM_EXCHANGE_NAME, true, false,arguments);
    }

    /*创建队列*/
    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME, true, false, false);
    }

    /*绑定*/
    @Bean
    public Binding confirmQueueBindConfirmExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }

    /*创建备份交换机*/
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME, true, false, null);
    }

    /*创建备份队列*/
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    /*创建报警队列*/
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    /*绑定*/
    @Bean
    public Binding backupQueueBindBackupExchange() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }

    /*绑定*/
    @Bean
    public Binding warningQueueBindBackupExchange() {
        return BindingBuilder.bind(warningQueue()).to(backupExchange());
    }
}
