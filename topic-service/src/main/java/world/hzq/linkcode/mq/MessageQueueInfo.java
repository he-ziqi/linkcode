package world.hzq.linkcode.mq;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/10 00:31
 */
public class MessageQueueInfo {
    private MessageQueueInfo(){}
    //消息主题类型信息
    public static class TopicTypeInfo{
        public static final String ADD_TOPIC_INFO_TOPIC = "add-topic-info";
        public static final String DELETE_TOPIC_INFO_ALL_TOPIC = "delete-topic-info-all";
        public static final String UPDATE_TOPIC_INFO_TOPIC = "update-topic-info";
        public static final String UPDATE_TOPIC_SOLUTION_INFO_TOPIC = "update-topic-solution-info";
        public static final String ADD_TOPIC_SOLUTION_INFO_TOPIC = "add-topic-solution-info";
    }
    //消费者组信息
    public static class ConsumerGroupInfo{
        //添加题目信息消费组
        public static final String ADD_TOPIC_INFO_GROUP = "add-topic-info-group";
        //添加题解信息消费者
        public static final String ADD_TOPIC_SOLUTION_INFO_GROUP = "add-topic-solution-info-group";
        //删除题目信息消费组(包括删除题解信息)
        public static final String DELETE_TOPIC_INFO_ALL_GROUP = "delete-topic-info-all-group";
        //更新题目信息消费组
        public static final String UPDATE_TOPIC_INFO_GROUP = "update-topic-info-group";
        //更新题解信息消费组
        public static final String UPDATE_TOPIC_SOLUTION_INFO_GROUP = "update-topic-solution-info-group";
    }
}