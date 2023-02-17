package world.hzq.linkcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/7 12:47
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("test","1024",1000,TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate);
    }

    @Test
    public void getTest(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String code = ops.get("test");
        System.out.println(code);
    }
}
