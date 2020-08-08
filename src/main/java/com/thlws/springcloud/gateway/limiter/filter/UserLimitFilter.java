package com.thlws.springcloud.gateway.limiter.filter;

import com.thlws.springcloud.gateway.internal.enums.LimiterEnum;
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

/**
 * @author HanleyTang 2020/8/2
 */
@Slf4j
@Component
public class UserLimitFilter implements GlobalFilter, Ordered {

    @Resource
    private LimitProcessor limitProcessor;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("user={}","Hanley");
        ServerHttpRequest request = exchange.getRequest();
//        request.getHeaders().add("user","hanley");
//        String user =  Objects.requireNonNull(request.getHeaders().get("user")).get(0);
        String user = "hanley";

        Mono<Object> limiter = reactiveRedisTemplate.opsForHash().get(LimiterEnum.USER.key(), user);
        return limiter.flatMap(e->{
            LimiterConfig config = (LimiterConfig)e;
            return limitProcessor.limit(exchange,chain,user,config);
        }).switchIfEmpty(chain.filter(exchange));

    }

    @Override
    public int getOrder() {
        return 0;
    }

}
