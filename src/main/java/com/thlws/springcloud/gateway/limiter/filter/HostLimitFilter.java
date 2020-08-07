package com.thlws.springcloud.gateway.limiter.filter;

import com.thlws.springcloud.gateway.limiter.LimitProcessor;
import com.thlws.springcloud.gateway.limiter.config.LimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author HanleyTang 2020/8/2
 */
@Slf4j
@Component
public class HostLimitFilter implements GlobalFilter, Ordered {

    @Resource
    private LimitProcessor limitProcessor;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest  request = exchange.getRequest();
        String host = Objects.requireNonNull(request.getRemoteAddress()).getHostName();
        log.info("host={}",host);
        Mono<Object> limiter = reactiveRedisTemplate.opsForHash().get("limiter:config:host", host);
        return limiter.flatMap(e->{
            LimiterConfig config = (LimiterConfig)e;
            return limitProcessor.limit(exchange,chain,host,config);
        }).switchIfEmpty(chain.filter(exchange));

    }

    @Override
    public int getOrder() {
        return 0;
    }

}
