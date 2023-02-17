package world.hzq.linkcode.mq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.es.TopicSolutionElasticSearchOperationExecutor;
import world.hzq.linkcode.mq.MessageQueueInfo;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/5 20:40
 */
@Component
@RocketMQMessageListener(topic = MessageQueueInfo.TopicTypeInfo.ADD_TOPIC_SOLUTION_INFO_TOPIC,
        consumerGroup = MessageQueueInfo.ConsumerGroupInfo.ADD_TOPIC_SOLUTION_INFO_GROUP)
public class AddTopicSolutionConsumer implements RocketMQListener<Solution> {
    private static final Logger logger = LoggerFactory.getLogger(AddTopicSolutionConsumer.class);

    @Autowired
    private TopicSolutionElasticSearchOperationExecutor topicSolutionElasticSearchOperationExecutor;

    @Override
    public void onMessage(Solution message) {
        logger.info("消费者消费消息(添加题解):{}",message);
        topicSolutionElasticSearchOperationExecutor.addTopicSolution(message);
    }
}
