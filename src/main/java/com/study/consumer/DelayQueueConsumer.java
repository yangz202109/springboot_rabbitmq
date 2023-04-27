package com.study.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @author yangz
 * @date 2022/5/28 - 9:34
 * 延迟队列(基于插件)·消费者
 */

@Slf4j
@Component
public class DelayQueueConsumer {

    @RabbitListener(queues = "DQN")
    public void receive(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间: {},收到的延迟队列的消息:{}", new Date(), msg);
    }
}
