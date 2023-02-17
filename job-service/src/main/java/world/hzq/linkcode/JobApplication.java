package world.hzq.linkcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 00:36
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class,args);
    }
}
