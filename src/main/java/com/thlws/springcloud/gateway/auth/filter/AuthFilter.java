package com.thlws.springcloud.gateway.auth.filter;

import com.thlws.springcloud.gateway.auth.AuthProcessor;
import com.thlws.springcloud.gateway.auth.config.AuthConfig;
import com.thlws.springcloud.gateway.internal.Const;
import com.thlws.springcloud.gateway.internal.util.PathUtil;
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
 * @author HanleyTang 2020/8/9
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Resource
    private AuthProcessor authProcessor;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("auth={}","myAuth");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        Flux<Object> auths = reactiveRedisTemplate.opsForSet().members(Const.Key.AUTH_API);
        Flux<Object> matchedAuthList = auths.filter(e->{
            AuthConfig config = (AuthConfig)e;
            return PathUtil.match(config.getPath(),path);
        });

        Mono<Object> matchedAuthOne = matchedAuthList.singleOrEmpty();
        return matchedAuthOne.flatMap(e->{
            AuthConfig config = (AuthConfig)e;
            return authProcessor.auth(exchange,chain,path,config);
        }).switchIfEmpty(chain.filter(exchange));

    }

    @Override
    public int getOrder() {
        return -1;
    }
}
