package world.hzq.linkcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 17:43
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TopicApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopicApplication.class,args);
    }
}
