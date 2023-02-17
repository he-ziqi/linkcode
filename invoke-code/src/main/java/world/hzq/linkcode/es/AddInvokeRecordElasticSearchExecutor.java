package world.hzq.linkcode.es;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.entity.SolutionRecord;
import world.hzq.linkcode.es.index.ElasticSearchIndexType;

import java.io.IOException;

/**
 * @author hzq
 * @version 1.0
 * @description 增加执行记录的es执行器
 * @date 2023/2/10 00:47
 */
@Slf4j
@Component
public class AddInvokeRecordElasticSearchExecutor extends BaseElasticSearchOperationExecutor{
    private static final String index = ElasticSearchIndexType.INDEX_SOLUTION_RECORD.getCode();

    /**
     * @description 添加题解记录信息
     * @param: solutionRecord
     * @author hzq
     * @date 2023/2/10 00:50
     */
    public void addSolutionInfo(SolutionRecord solutionRecord){
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(JSONObject.toJSONString(solutionRecord), XContentType.JSON);
        try {
            log.info("添加代码执行记录信息:{}",solutionRecord);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("添加代码执行记录信息失败:{},异常信息:{}",solutionRecord,e.getMessage());
            throw new RuntimeException();
        }
    }
}
