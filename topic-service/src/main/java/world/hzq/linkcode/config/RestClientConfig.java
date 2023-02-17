package world.hzq.linkcode.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author hzq
 * @version 1.0
 * @description es配置类
 * @date 2023/2/4 14:02
 */
//@Configuration
public class RestClientConfig{
    //@Value("${spring.elasticsearch.}")
    private String esHost;
    private String esPort;
    //@Bean
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    }
}
