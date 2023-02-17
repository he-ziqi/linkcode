package world.hzq.linkcode.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.es.TopicInfoElasticSearchOperationExecutor;
import world.hzq.linkcode.mq.MessageQueueInfo;


/**
 * @author hzq
 * @version 1.0
 * @description 增加题目信息消费者
 * @date 2023/2/5 02:48
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = MessageQueueInfo.TopicTypeInfo.ADD_TOPIC_INFO_TOPIC,
        consumerGroup = MessageQueueInfo.ConsumerGroupInfo.ADD_TOPIC_INFO_GROUP)
public class AddTopicInfoConsumer implements RocketMQListener<Topic> {

    private static final Logger logger = LoggerFactory.getLogger(AddTopicInfoConsumer.class);

    @Autowired
    private TopicInfoElasticSearchOperationExecutor topicInfoElasticSearchOperationExecutor;

    //消费消息
    @Override
    public void onMessage(Topic message) {
        logger.info("消费者消费(添加)消息:{}",message);
        topicInfoElasticSearchOperationExecutor.addTopicInfo(message);
    }
}
