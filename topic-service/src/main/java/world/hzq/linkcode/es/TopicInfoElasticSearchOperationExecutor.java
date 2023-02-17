package world.hzq.linkcode.es;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.es.index.ElasticSearchIndexType;
import world.hzq.linkcode.exception.ElasticSearchOperationException;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.vo.InfoVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 题目信息es操作执行器
 * @date 2023/2/5 17:56
 */
@Component
@Slf4j
public class TopicInfoElasticSearchOperationExecutor extends BaseElasticOperationExecutor{

    private static final Logger logger = LoggerFactory.getLogger(TopicInfoElasticSearchOperationExecutor.class);

    private static final String index = ElasticSearchIndexType.INDEX_TOPIC_INFO.getCode();

    /**
     * @description 根据题目id更新题目信息
     * @param: topic  题目信息
     * @author hzq
     * @date 2023/2/5 18:44
     */
    public void updateTopicInfo(Topic topic){
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(index);
        updateByQueryRequest.setRefresh(true); //设置更新后刷新索引
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("id",topic.getId()));
        //updateByQueryRequest.setConflicts("proceed");
        updateByQueryRequest.setQuery(boolQueryBuilder);
        //设置更新内容
        Script script = convertToScript(topic);
        System.out.println(script.toString());
        updateByQueryRequest.setScript(script);
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("更新题目信息失败,异常信息:{}",e.getMessage());
            //失败 回滚
            throw new RuntimeException();
        }
        if(response.getUpdated() == 0){
            logger.info("更新题目信息失败,消息:{}未更新",topic);
            throw new RuntimeException();
        }
        logger.info("题目信息更新成功,更新条数:{}",response.getUpdated());
    }

    /**
     * @description  删除题目信息
     * @param: topicId  题目id
     * @author hzq
     * @date 2023/2/5 18:03
     */
    public void deleteTopicInfo(Long topicId){
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(index);
        deleteByQueryRequest.setRefresh(true); //设置刷新索引
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("id",topicId));
        deleteByQueryRequest.setQuery(boolQueryBuilder);
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("删除题目信息失败,异常信息:{}",e.getMessage());
            throw new RuntimeException();
        }
        if(response.getDeleted() == 0){
            logger.info("删除题目信息失败,消息:{}未删除",topicId);
            throw new RuntimeException();
        }
        logger.info("删除题目信息成功,共删除:{}条信息",response.getDeleted());
    }

    /**
     * @description 添加题目信息
     * @param: topic  题目实体
     * @author hzq
     * @date 2023/2/5 18:14
     */
    public void addTopicInfo(Topic topic) {
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(JSONObject.toJSONString(topic), XContentType.JSON);
        //发送请求
        try {
            logger.info("添加题目信息：{}",topic);
            //向es索引添加数据
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("添加题目信息失败：{} 失败,抛出异常,添加题目信息操作回滚,异常信息：{}",topic,e.getMessage());
            //插入es失败,抛出异常,让添加题目操作回滚
            throw new RuntimeException();
        }
    }

    /**
     * @description 获取题目信息
     * @param: topicName 题目名称
     * @param: topicComment 题目描述内容
     * @param: pageNo 页码
     * @param: pageSize 分页大小
     * @param: difficulty 难度等级
     * @return: world.hzq.sjm.vo.InfoVO<world.hzq.sjm.entity.Topic>
     * @author hzq
     * @date 2023/2/5 18:16
     */
    public InfoVO<Topic> getTopicInfo(String topicName, String topicComment, Integer pageNo, Integer pageSize, Integer difficulty) throws ElasticSearchOperationException {
        //构造es请求对象
        //设置要查询的es索引名称
        SearchRequest searchRequest = new SearchRequest(index);
        //条件对象构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //初始化条件对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(Tools.isNotNull(topicName)){
            boolQueryBuilder.should(new MatchQueryBuilder("topicName",topicName));
        }
        if(Tools.isNotNull(topicComment)){
            boolQueryBuilder.should(new MatchQueryBuilder("topicComment",topicComment));
        }
        //设置分页
        searchSourceBuilder.from((pageNo - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        //将条件设置到条件构造器中
        searchSourceBuilder.query(boolQueryBuilder);
        //设置排序(先按难度排序,后按题号升序排序)
        if(Tools.isNotNull(difficulty)){
            searchSourceBuilder.sort("difficultyLevel",difficulty == 0 ? SortOrder.ASC : SortOrder.DESC);
        }
        searchSourceBuilder.sort("id", SortOrder.ASC);
        //将条件构造器对象设置到请求对象中
        searchRequest.source(searchSourceBuilder);
        //执行检索
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.info("查询题目信息异常:{}",e.getMessage());
            e.printStackTrace();
            throw new ElasticSearchOperationException();
        }
        SearchHits hits = response.getHits();
        List<Topic> topicList = new ArrayList<>(hits.getHits().length);
        hits.forEach(hit -> {
            String resultStr = hit.getSourceAsString();
            Topic topic = JSONObject.parseObject(resultStr, Topic.class);
            topicList.add(topic);
        });
        return new InfoVO<>(topicList,response.getHits().getTotalHits().value);
    }
}
