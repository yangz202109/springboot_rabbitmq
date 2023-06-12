package com.study.controller;

import com.study.config.ConfirmConfig;
import com.study.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

/**
 * @author yangz
 * @date 2022/5/24 - 17:02
 */
@Slf4j
@RestController
public class SendMsgController {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SendMsgController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 普通交换机名称
     */
    @Value("${rabbitmq.exchange.normal}")
    private String NORMAL_EXCHANGE;

    /**
     * 发送消息
     */
    @GetMapping("/sendMessage")
    public void sendMessage(String message) {

        log.info("当前时间: {},开始发送消息", new Date());

        /*发送消息到普通交换机中绑定指定routingKey的队列*/
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, "XA", message);
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, "XB", message);
    }

    /**
     * 发送定时消息
     */
    @GetMapping("/sendMessageAndTime")
    public void sendMessageAndTime(String message, String time) {

        log.info("当前时间: {},开始发送消息,在{}秒后发送", new Date(), time);

        /*发送消息到普通交换机中绑定指定routingKey的队列*/
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, "XC", message, msg -> {
            /*设置消息过期时间*/
            msg.getMessageProperties().setExpiration(time);
            return msg;
        });
    }

    /**
     * 发送延迟消息
     */
    @GetMapping("/sendDelayMsg")
    public void sendDelayMsg(String message, Integer time) {
        log.info("当前时间: {},开始发送消息", new Date());

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, message,
                msg -> {
                    /*设置发送消息 延迟时长 单位 ms*/
                    msg.getMessageProperties().setDelay(time);
                    return msg;
                }
        );
    }

    @GetMapping("/publish")
    public void publish(String message) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData);


        CorrelationData correlationData2 = new CorrelationData();
        correlationData2.setId("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "11", message, correlationData2);

        log.info("发布消息完毕");
    }
}
