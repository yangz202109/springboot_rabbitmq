package com.study.consumer;

import com.study.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author yangz
 * @date 2022/5/31 - 11:27
 */
@Slf4j
@Component
public class WarningQueueConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receive(Message message){
        String msg = new String(message.getBody());
        log.info("报警队列收到消息 :{}",msg);
    }
}
