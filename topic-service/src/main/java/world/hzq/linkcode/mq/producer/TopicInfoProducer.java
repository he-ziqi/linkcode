package world.hzq.linkcode.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hzq
 * @version 1.0
 * @description 题目信息生产者
 * @date 2023/2/5 02:43
 */
@Component
@Slf4j
public class TopicInfoProducer {
    private static final Logger logger = LoggerFactory.getLogger(TopicInfoProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    //发送异步消息
    public void sendMessage(String topic,Object message){
        //向topic发送异步消息
        //rocketMQTemplate.convertAndSend(topic,message);
        logger.info("生产者准备发送消息,主题：{},消息:{}",topic,message);
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            //消息发送成功的回调
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("消息发送成功,messageId:{},message:{}",sendResult.getMsgId(),message);
            }

            //发送失败的回调
            @Override
            public void onException(Throwable throwable) {
                logger.info("消息发送失败,message:{},错误信息:{}",message,throwable.getMessage());
                throw new RuntimeException();
            }
        });
    }
}
