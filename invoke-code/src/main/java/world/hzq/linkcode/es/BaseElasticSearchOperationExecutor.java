package world.hzq.linkcode.es;

import org.elasticsearch.client.RestHighLevelClient;

import javax.annotation.Resource;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/10 00:44
 */
public class BaseElasticSearchOperationExecutor {
    @Resource
    public RestHighLevelClient restHighLevelClient;
}
