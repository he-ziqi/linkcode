package world.hzq.linkcode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import world.hzq.linkcode.util.Result;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/9 07:34
 */
@FeignClient(name = "user-service",path = "/info")
public interface UserFeign {
    //获取用户编译目录
    @GetMapping("/get/compile/path")
    Result<String> getCompilePath();
}
