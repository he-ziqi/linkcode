package world.hzq.linkcode.es;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.es.index.ElasticSearchIndexType;
import world.hzq.linkcode.exception.ElasticSearchOperationException;
import world.hzq.linkcode.vo.InfoVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 题解es操作执行器
 * @date 2023/2/5 17:58
 */
@Component
@Slf4j
public class TopicSolutionElasticSearchOperationExecutor extends BaseElasticOperationExecutor{
    private static final Logger logger = LoggerFactory.getLogger(TopicSolutionElasticSearchOperationExecutor.class);

    private static final String index = ElasticSearchIndexType.INDEX_TOPIC_SOLUTION.getCode();

    /**
     * @description 删除题解
     * @param: topicId  题目id
     * @author hzq
     * @date 2023/2/5 17:45
     */
    public void deleteTopicSolution(Long topicId){
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(index);
        deleteByQueryRequest.setRefresh(true); //刷新索引
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("topicId",topicId));
        deleteByQueryRequest.setQuery(boolQueryBuilder);
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            //失败,回滚
            logger.info("删除题解信息失败,异常信息:{}",e.getMessage());
            throw new RuntimeException();
        }
        long deleted = response.getDeleted();
        if(deleted == 0){
            logger.info("删除题解信息失败,消息:{}未删除",topicId);
            throw new RuntimeException();
        }
        logger.info("删除题解信息成功,共删除：{}条信息",deleted);
    }

    /**
     * @description 获取题解信息
     * @param: topicId 题目id
     * @param: languageType 语言类型
     * @param: pageNo 页码
     * @param: pageSize 分页大小
     * @return: world.hzq.sjm.vo.InfoVO<world.hzq.sjm.entity.Solution>
     * @author hzq
     * @date 2023/2/5 18:10
     */
    public InfoVO<Solution> getTopicSolutionInfo(Long topicId, String languageType,Integer pageNo,Integer pageSize) throws ElasticSearchOperationException {
        //从es中获取题解信息
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("topicId",topicId));
        boolQueryBuilder.must(new TermQueryBuilder("languageType",languageType));
        searchSourceBuilder.from((pageNo - 1)*pageSize);
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.query(boolQueryBuilder);
        //按题解创建时间升序
        searchSourceBuilder.sort("createTime", SortOrder.ASC);
        searchRequest.source(searchSourceBuilder);
        //执行检索
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.info("查询题解信息异常:{}",e.getMessage());
            e.printStackTrace();
            throw new ElasticSearchOperationException();
        }
        SearchHits hits = response.getHits();
        List<Solution> solutionList = new ArrayList<>(hits.getHits().length);
        hits.forEach(hit -> {
            String resultStr = hit.getSourceAsString();
            Solution solution = JSONObject.parseObject(resultStr, Solution.class);
            solutionList.add(solution);
        });
        return new InfoVO<>(solutionList,response.getHits().getTotalHits().value);
    }

    /**
     * @description 通过题解id更新题解信息
     * @param: solutionList
     * @author hzq
     * @date 2023/2/5 18:55
     */
    public void updateTopicSolutionInfo(List<Solution> solutionList) {
        solutionList.forEach(this::updateTopicSolutionInfo);
    }

    public void updateTopicSolutionInfo(Solution solution){
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(index);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("id",solution.getId()));
        updateByQueryRequest.setQuery(boolQueryBuilder);
        updateByQueryRequest.setRefresh(true); //设置刷新索引
        //设置更新内容
        Script script = convertToScript(solution);
        updateByQueryRequest.setScript(script);
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("更新题解信息失败,异常信息:{}",e.getMessage());
            throw new RuntimeException();
        }
        if(response.getUpdated() == 0){
            logger.info("更新题解信息失败,消息:{}未更新",solution);
            throw new RuntimeException();
        }
        logger.info("题目信息更新成功,更新条数:{}",response.getUpdated());
    }

    /**
     * @description 添加题解
     * @param: solution 题解对象
     * @author hzq
     * @date 2023/2/5 20:44
     */
    public void addTopicSolution(Solution solution) {
        IndexRequest indexRequest = new IndexRequest(index);
        logger.info("es添加题解:{}",solution);
        indexRequest.source(JSONObject.toJSONString(solution), XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("添加题解信息失败：{},异常信息:{}",solution,e.getMessage());
        }
    }
}
