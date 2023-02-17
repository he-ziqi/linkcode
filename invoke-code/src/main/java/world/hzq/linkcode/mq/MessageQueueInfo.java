package world.hzq.linkcode.mq;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/10 00:33
 */
public class MessageQueueInfo {
    private MessageQueueInfo(){}
    //消息主题类型信息
    public static class TopicTypeInfo{
        public static final String ADD_INVOKE_RECORD_TOPIC = "add-invoke-record-info";
    }
    //消费者组信息
    public static class ConsumerGroupInfo{
        //添加执行记录信息消费组
        public static final String ADD_INVOKE_RECORD_GROUP = "add-invoke-record-group";
    }
}
