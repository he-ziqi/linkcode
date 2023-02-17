package world.hzq.linkcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.es.TopicInfoElasticSearchOperationExecutor;
import world.hzq.linkcode.es.TopicSolutionElasticSearchOperationExecutor;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/4 14:20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {

    @Autowired
    private TopicInfoElasticSearchOperationExecutor info;

    @Autowired
    private TopicSolutionElasticSearchOperationExecutor solution;

    @Test
    public void test(){
        Topic topic = new Topic();
        topic.setId(1000L);
        topic.setTopicName("update-name");
        topic.setTopicComment("update-comment");
        topic.setDifficultyLevel((byte) 10);
        info.updateTopicInfo(topic);
    }

   /* @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testEs() throws IOException {
        System.out.println("s:========"+restHighLevelClient);
        //构造es请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("topic_info");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder bool = new BoolQueryBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("topic_name","反转");
        bool.must(new TermQueryBuilder("difficulty_level","2"));
        bool.must(matchQueryBuilder);
        //bool.filter(termQueryBuilder);
        //设置检索条件
        searchSourceBuilder.query(bool);
        //将检索构造器设置到请求对象中
        searchRequest.source(searchSourceBuilder);
        //执行检索
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(hits.getHits().length);
        for (SearchHit hit : hits) {
            String res = hit.getSourceAsString();
            Topic topic = JSONObject.parseObject(res, Topic.class);
            System.out.println(topic);
            System.out.println(hit.getId());
            System.out.println(hit.getIndex());
        }
    }

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testMQ() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("topic-service");
        producer.start();
        String body = "userName:'user1',age:23";
        Message message = new Message("topic-service","topic-tag",body.getBytes());
        SendResult res = producer.send(message);
        System.out.println(res);
        producer.shutdown();
        String body = "userName:'user1',age:23";
        Message message = new Message("topic-service","topic-tag",body.getBytes());

        rocketMQTemplate.syncSendOrderly("topic-service:tag1",body,"1111");
        //rocketMQTemplate.sendAndReceive("topic-service",body,);
    }

    @Test
    public void testConsumer() throws MQClientException {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("topic-service");

        // 设置NameServer的地址
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeTimeout(10000);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("TopicTest", "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }*/

}
