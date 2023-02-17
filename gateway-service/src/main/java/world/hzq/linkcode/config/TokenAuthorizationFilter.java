package world.hzq.linkcode.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import world.hzq.linkcode.util.CommonUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description token过滤器 内部接口过滤通过后执行
 * @date 2023/2/8 19:48
 */
@Component
@Slf4j
public class TokenAuthorizationFilter implements GlobalFilter, Ordered {

    @Autowired
    private UnLoginAllowUrl allowUrl;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RoleAuthorityFilter roleAuthorityFilter;

    //token续签时间阈值
    @Value("${token.refresh}")
    private Long tokenRefreshThreshold;

    //token续签阈值
    @Value("${token.threshold}")
    private Integer refreshThreshold;

    @Value("${token.timeout}")
    private Long timeout;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        //做ip访问记录
        String path = request.getURI().getPath();
        String ip = CommonUtil.getIpAddress(request);
        log.info("ip:{},请求接口:{}",ip,request.getURI().getPath());

        if(allowUrl.allow(path,allowUrl.getAllow()) || allowUrl.allow(path,allowUrl.getAllowInterfaceCenter())){
            //白名单直接放行
            return chain.filter(exchange);
        }
        List<String> authorization = request.getHeaders().get("Authorization");
        if(!CollectionUtils.isEmpty(authorization)){
            String tokenStr = authorization.get(0);
            //非空字符串
            if(tokenStr != null && !"".equals(tokenStr) && tokenStr.startsWith("bearer ")){
                String token = tokenStr.replaceFirst("bearer ", "");
                //判断token是否为null 并且redis是否包含此token
                if(!"".equals(token)){
                    //获取token剩余时间
                    Long time = stringRedisTemplate.opsForValue().getOperations().getExpire(token);
                    if(time != null){
                        log.info("token剩余时间：{}秒",time);
                        //剩余时间小于阈值则自动续签
                        if (time > 1 && time <= tokenRefreshThreshold) {
                            //判断此token续签次数
                            String thresholdStr = stringRedisTemplate.opsForValue().get(token + ":threshold");
                            int threshold = Integer.parseInt(thresholdStr == null ? "0" : thresholdStr);
                            //在续签次数允许只内
                            if(threshold < refreshThreshold){
                                String userJson = stringRedisTemplate.opsForValue().get(token);
                                if(refreshToken(token,userJson,threshold + 1)){
                                    //续签成功 失败则为非法请求
                                    log.info("用户：{},token续签成功,目前已续签：{}次",userJson,threshold);
                                    return roleAuthorityFilter.filter(exchange, chain); //放行到角色过滤器
                                }
                            }
                        }
                        //剩余时间还长 直接放行
                        if(time > tokenRefreshThreshold){
                            return roleAuthorityFilter.filter(exchange, chain); //放行到角色过滤器
                        }
                    }
                }
            }
        }
        //非法访问,驳回请求
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("content-type","application/json;charset=utf-8");
        Map<String,Object> map = new HashMap<>();
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("msg","未授权,请先登录");
        byte[] bytes = new byte[0];
        try {
            bytes = new ObjectMapper().writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return 1;
    }

    //true表示token刷新成功
    private boolean refreshToken(String token,String userJson,Integer refreshThreshold){
        if(userJson == null){
            //错误token 非法请求
            return false;
        }
        //token刷新次数+1
        stringRedisTemplate.opsForValue().set(token + ":threshold", String.valueOf(refreshThreshold),2 * timeout,TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(token,userJson,timeout, TimeUnit.SECONDS);
        return true;
    }

}
