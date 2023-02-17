package world.hzq.linkcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 00:20
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ClassRoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClassRoomApplication.class,args);
    }
}
