package world.hzq.linkcode.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.es.TopicInfoElasticSearchOperationExecutor;
import world.hzq.linkcode.es.TopicSolutionElasticSearchOperationExecutor;
import world.hzq.linkcode.mq.MessageQueueInfo;

/**
 * @author hzq
 * @version 1.0
 * @description 删除题目信息消费者
 * @date 2023/2/5 16:52
 */
@Component
@RocketMQMessageListener(topic = MessageQueueInfo.TopicTypeInfo.DELETE_TOPIC_INFO_ALL_TOPIC,
        consumerGroup = MessageQueueInfo.ConsumerGroupInfo.DELETE_TOPIC_INFO_ALL_GROUP)
@Slf4j
public class DeleteTopicInfoConsumer implements RocketMQListener<Long> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteTopicInfoConsumer.class);

    @Autowired
    private TopicSolutionElasticSearchOperationExecutor topicSolutionElasticSearchOperationExecutor;

    @Autowired
    private TopicInfoElasticSearchOperationExecutor topicInfoElasticSearchOperationExecutor;

    /**
     * @description 消费删除题目信息消息
     * @param: message 题目id
     * @author hzq
     * @date 2023/2/5 16:55
     */
    @Override
    public void onMessage(Long message) {
        logger.info("消费者开始消费删除消息:{}",message);
        //删除题解
        topicSolutionElasticSearchOperationExecutor.deleteTopicSolution(message);
        //删除题目信息
        topicInfoElasticSearchOperationExecutor.deleteTopicInfo(message);
        logger.info("消费者消费删除消息结束:{}",message);
    }


}
