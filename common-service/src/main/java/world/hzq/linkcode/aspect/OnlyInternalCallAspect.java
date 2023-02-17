package world.hzq.linkcode.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import world.hzq.linkcode.exception.OnlyInternalCallException;
import world.hzq.linkcode.util.Tools;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hzq
 * @version 1.0
 * @description 内部调用切面
 * @date 2023/2/3 15:47
 */
@Aspect
@Component
@Slf4j
public class OnlyInternalCallAspect {
    private static final Logger logger = LoggerFactory.getLogger(OnlyInternalCallAspect.class);
    @Pointcut("@annotation(world.hzq.linkcode.annotation.OnlyInternalCall)")
    public void onlyInternalCallMethod(){}

    @Before("onlyInternalCallMethod()")
    public void before(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String from = request.getHeader("from");
        if(Tools.isNotNull(from) && Tools.equals("public",from)){
            logger.error("内部api,不允许外部调用");
            throw new OnlyInternalCallException("内部api,不允许外部调用");
        }
    }
}
