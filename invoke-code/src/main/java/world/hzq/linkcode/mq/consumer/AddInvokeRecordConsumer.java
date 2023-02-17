package world.hzq.linkcode.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.SolutionRecord;
import world.hzq.linkcode.es.AddInvokeRecordElasticSearchExecutor;
import world.hzq.linkcode.mq.MessageQueueInfo;

/**
 * @author hzq
 * @version 1.0
 * @description 添加代码执行记录消费者
 * @date 2023/2/10 00:36
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = MessageQueueInfo.TopicTypeInfo.ADD_INVOKE_RECORD_TOPIC,
        consumerGroup = MessageQueueInfo.ConsumerGroupInfo.ADD_INVOKE_RECORD_GROUP)
public class AddInvokeRecordConsumer implements RocketMQListener<SolutionRecord> {

    @Autowired
    private AddInvokeRecordElasticSearchExecutor addInvokeRecordElasticSearchExecutor;

    @Override
    public void onMessage(SolutionRecord message) {
        log.info("消费者添加代码执行记录消息:{}",message);
        addInvokeRecordElasticSearchExecutor.addSolutionInfo(message);
    }
}
