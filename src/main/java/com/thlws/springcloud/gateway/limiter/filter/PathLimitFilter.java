package com.thlws.springcloud.gateway.limiter.filter;

import com.thlws.springcloud.gateway.internal.util.PathUtil;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author HanleyTang 2020/8/2
 */
@Slf4j
@Component
public class PathLimitFilter implements GlobalFilter, Ordered {

    @Resource
    private LimitProcessor limitProcessor;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest  request = exchange.getRequest();
        String path = request.getPath().value();
        Flux<Object> apis = reactiveRedisTemplate.opsForHash().keys("limiter:config:api");
        Flux<Object> matchedApiList = apis.filter(e-> PathUtil.match(e.toString(),path));
        Mono<Object> matchedApiOne = matchedApiList.singleOrEmpty();

        return matchedApiOne.flatMap(e->{
            Mono<Object> matchedConfig = reactiveRedisTemplate.opsForHash().get("limiter:config:api", e.toString());
            return matchedConfig.flatMap(one->{
                LimiterConfig config = (LimiterConfig)one;
                log.info("gateway path limiter path=[{}],config=[{}]",path,config.toString());
                return limitProcessor.limit(exchange,chain,path,config);
            });
        }).switchIfEmpty(chain.filter(exchange));

    }

    @Override
    public int getOrder() {
        return 1;
    }

}
