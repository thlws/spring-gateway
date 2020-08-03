package com.thlws.springcloud.gateway.config;

import com.alibaba.fastjson.JSON;
import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.limiter.LimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author HanleyTang 2020/8/2
 */
@Slf4j
@Component
public class LimiterGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private CustomRedisRateLimiter customRedisRateLimiter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest  request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().value();
        log.info("请求路径:{}",path);

        if (path.contains("/flow")) {

            LimiterConfig config = LimiterConfig.builder()
                    .burstCapacity(1).replenishRate(1).requestedTokens(1).build();
            Mono<RateLimiter.Response> responseMono = customRedisRateLimiter.isAllowed(path, config);

            return responseMono.flatMap(r -> {
                for (Map.Entry<String, String> header : r.getHeaders().entrySet()) {
                    exchange.getResponse().getHeaders().add(header.getKey(),header.getValue());
                }

                if (r.isAllowed()) {
                    return chain.filter(exchange);
                }

                ApiResult<String> apiResult = ApiResult.error(HttpStatus.TOO_MANY_REQUESTS.value(), "请求过于频繁.");
                byte [] bytes = JSON.toJSONString(apiResult).getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));

            });

        }

        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return -6;
    }
}
