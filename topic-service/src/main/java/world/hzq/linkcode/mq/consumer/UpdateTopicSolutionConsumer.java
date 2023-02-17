package world.hzq.linkcode.mq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.es.TopicSolutionElasticSearchOperationExecutor;
import world.hzq.linkcode.mq.MessageQueueInfo;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 更新题解信息消费者
 * @date 2023/2/5 18:39
 */
@Component
@RocketMQMessageListener(topic = MessageQueueInfo.TopicTypeInfo.UPDATE_TOPIC_SOLUTION_INFO_TOPIC
        ,consumerGroup = MessageQueueInfo.ConsumerGroupInfo.UPDATE_TOPIC_SOLUTION_INFO_GROUP)
public class UpdateTopicSolutionConsumer implements RocketMQListener<List<Solution>> {

    @Autowired
    private TopicSolutionElasticSearchOperationExecutor topicSolutionElasticSearchOperationExecutor;

    @Override
    public void onMessage(List<Solution> message) {
        topicSolutionElasticSearchOperationExecutor.updateTopicSolutionInfo(message);
    }
}
