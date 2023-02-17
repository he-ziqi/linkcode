package world.hzq.linkcode.annotation;

import java.lang.annotation.*;

/**
 * @author hzq
 * @version 1.0
 * @description 仅内部调用注解
 * @date 2023/2/3 15:50
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyInternalCall {
}
