package world.hzq.linkcode;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/7 17:12
 */
@SpringBootTest
public class TestRedis {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test(){
        System.out.println(stringRedisTemplate);
    }
}
