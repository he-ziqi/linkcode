package world.hzq.linkcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import world.hzq.linkcode.util.ApplicationUtil;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 17:27
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class InvokeCodeApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(InvokeCodeApplication.class, args);
        ApplicationUtil.setApplicationContext(applicationContext);
    }
}
