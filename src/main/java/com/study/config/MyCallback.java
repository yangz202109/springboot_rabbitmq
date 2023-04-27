package com.study.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author yangz
 * @date 2022/5/30 - 9:55
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        /*注入,自定义的回调接口实现类*/
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机回调(无论是否接送到消息都会触发执行)
     * @param correlationData 保存回调消息的ID及相关信息，发送消息的时候填写
     * @param b               交换机是否接收到消息
     * @param s               消息接收失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : null;
        if (b) {
            log.info("交换机成功接送到消息 id={}", id);
        } else {
            log.info("交换机没有接收到id={} 的消息,由于原因:{}", id, s);
        }
    }

    /**
     * 队列回调(只有当消息无法传递到不可routingKey)
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息相应码: {}", returnedMessage.getReplyCode());
        log.error("消息主体: {}", returnedMessage.getMessage());
        log.error("描述: {}", returnedMessage.getReplyText());
        log.error("消息使用的交换机 : {}", returnedMessage.getExchange());
        log.error("消息使用的路由key: {}", returnedMessage.getRoutingKey());
    }


}
