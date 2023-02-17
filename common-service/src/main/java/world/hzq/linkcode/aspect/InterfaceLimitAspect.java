package world.hzq.linkcode.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import world.hzq.linkcode.annotation.InterfaceLimit;
import world.hzq.linkcode.util.IPUtil;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description 接口请求限制切面
 * @date 2023/2/7 16:04
 */
@Aspect
@Component
@Slf4j
public class InterfaceLimitAspect {
    @Pointcut("@annotation(world.hzq.linkcode.annotation.InterfaceLimit)")
    public void pointcut(){}

    private static Map<String, Map<String,Integer>> requestLimitMap = new HashMap<>();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> targetCls = pjp.getTarget().getClass();
        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        //获取目标方法上的注解指定的操作名称
        Method targetMethod = targetCls.getDeclaredMethod(
                        ms.getName(),
                        ms.getParameterTypes());
        //获取接口上限流注解的信息
        InterfaceLimit interfaceLimit = targetMethod.getAnnotation(InterfaceLimit.class);
        long time = interfaceLimit.time();
        int count = interfaceLimit.count();
        TimeUnit timeunit = interfaceLimit.timeunit();
        // 获得request对象
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        //获取ip
        String ip = IPUtil.getRealIp(request);
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //此ip已被限制
        if(Tools.isNotNull(ops.get(ip + ":" + request.getRequestURI()))){
            log.info("ip:{},尝试访问接口:{},但此ip已被限流,访问失败",ip,request.getRequestURI());
            return Result.fail("接口超过请求次数,请稍后重试");
        }
        Map<String, Integer> ipLimitMap = requestLimitMap.get(ip);
        //ip限制map为null则为第一次访问
        if(Tools.isNull(ipLimitMap)){
            HashMap<String, Integer> map = new HashMap<>();
            //存入此ip访问的接口路径 并设置为第一次访问
            map.put(request.getRequestURI(),1);
            //存入此ip和限制访问map
            requestLimitMap.put(ip,map);
        }else{ //限制集合存在
            Integer cnt = ipLimitMap.get(request.getRequestURI());
            if(cnt == null){ //第一次访问此接口
                ipLimitMap.put(request.getRequestURI(),1);
            }else if(cnt + 1 > count){ //此接口访问超过规定次数
                //存入redis 设置过期时间
                stringRedisTemplate.opsForValue().set(ip + ":" + request.getRequestURI(),"limit",time,timeunit);
                log.info(ip + "访问接口" + request.getRequestURI() + "超过限定次数:" + count + "次,请求驳回");
                //重置当前ip此接口的请求次数,由redis进行限制
                ipLimitMap.put(request.getRequestURI(),null);
                return Result.fail("请求超过次数,请稍后重试");
            }else {
                //访问次数 + 1
                ipLimitMap.put(request.getRequestURI(),cnt + 1);
            }
        }
        //通过限制 执行目标方法
        return pjp.proceed();
    }
}
