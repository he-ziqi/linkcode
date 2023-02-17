package world.hzq.linkcode.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description 接口请求限制注解
 * @date 2023/2/7 16:05
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceLimit {
    //限制访问时间
    long time() default 180;
    //限制访问次数
    int count() default 1;
    //时间单位
    TimeUnit timeunit() default TimeUnit.SECONDS;
}
