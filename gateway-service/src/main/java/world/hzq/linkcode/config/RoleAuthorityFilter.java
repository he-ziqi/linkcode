package world.hzq.linkcode.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import world.hzq.linkcode.util.CommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hzq
 * @version 1.0
 * @description 角色权限过滤器 token校验通过后执行
 * @date 2023/2/13 16:09
 */
@Component
@Slf4j
public class RoleAuthorityFilter{

    @Autowired
    private RoleAllowUrl roleAllowUrl;

    @Autowired
    private UnLoginAllowUrl allowUrl;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //接口中心的请求直接放行
        if(allowUrl.allow(path,allowUrl.getAllowInterfaceCenter())){
            return chain.filter(exchange);
        }
        String token = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0).replaceFirst("bearer ", "");
        DecodedJWT jwt = CommonUtil.verifyToken(token);
        //获取用户角色名称
        String roleType = jwt.getClaim("roleType").asString();
        List<String> notAllowUrlList = null;
        if("teacher_type".equals(roleType)){
            notAllowUrlList = roleAllowUrl.getNotAllowTeacher();
        }else{
            notAllowUrlList = roleAllowUrl.getNotAllowStudent();
        }
        if(roleAllowUrl.allow(path,notAllowUrlList)){
            //如果此请求路径是不允许的就拦截
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set("content-type","application/json;charset=utf-8");
            Map<String,Object> map = new HashMap<>();
            map.put("code", HttpStatus.UNAUTHORIZED.value());
            map.put("msg","您无权限访问");
            byte[] bytes = new byte[0];
            try {
                bytes = new ObjectMapper().writeValueAsBytes(map);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        }
        //允许的请求放行
        return chain.filter(exchange);
    }
}
