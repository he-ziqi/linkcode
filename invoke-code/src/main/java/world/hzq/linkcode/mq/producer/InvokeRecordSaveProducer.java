package world.hzq.linkcode.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hzq
 * @version 1.0
 * @description 执行记录存储生产者
 * @date 2023/2/10 00:14
 */
@Component
@Slf4j
public class InvokeRecordSaveProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic,Object message){
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送成功,messageId:{},message:{}",sendResult.getMsgId(),message);
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("消息发送失败,message:{},错误信息:{}",message,throwable.getMessage());
            }
        });
    }
}
