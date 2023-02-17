package world.hzq.linkcode.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author hzq
 * @version 1.0
 * @description 内部调用过滤器
 * @date 2023/2/3 15:41
 */
@Component
public class InternalCallFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //对经过网关的请求加上from:public请求头
        return chain.filter(
                exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header("from","public")
                                .build())
                        .build()
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
