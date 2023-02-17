package world.hzq.linkcode.mq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.es.TopicInfoElasticSearchOperationExecutor;
import world.hzq.linkcode.mq.MessageQueueInfo;

/**
 * @author hzq
 * @version 1.0
 * @description 更新题目信息消费者
 * @date 2023/2/5 18:38
 */
@Component
@RocketMQMessageListener(topic = MessageQueueInfo.TopicTypeInfo.UPDATE_TOPIC_INFO_TOPIC,
        consumerGroup = MessageQueueInfo.ConsumerGroupInfo.UPDATE_TOPIC_INFO_GROUP)
public class UpdateTopicInfoConsumer implements RocketMQListener<Topic> {
    @Autowired
    private TopicInfoElasticSearchOperationExecutor topicInfoElasticSearchOperationExecutor;

    @Override
    public void onMessage(Topic topic) {
        topicInfoElasticSearchOperationExecutor.updateTopicInfo(topic);
    }
}
