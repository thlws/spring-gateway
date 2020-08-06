package com.thlws.springcloud.gateway.limiter;

import com.alibaba.fastjson.JSON;
import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.limiter.config.LimiterConfig;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author HanleyTang 2020/8/5
 */
@Component
public class LimitProcessor {

    @Resource
    private MyRateLimiter customRedisRateLimiter;

    public Mono<Void> limit(ServerWebExchange exchange,
                             GatewayFilterChain chain,
                             String key,
                             LimiterConfig config){

        Mono<RateLimiter.Response> responseMono = customRedisRateLimiter.isAllowed(key, config);
        return responseMono.flatMap(r -> {

            for (Map.Entry<String, String> header : r.getHeaders().entrySet()) {
                exchange.getResponse().getHeaders().add(header.getKey(),header.getValue());
            }

            if (r.isAllowed()) {
                return chain.filter(exchange);
            }

            ApiResult<String> apiResult = ApiResult.error(HttpStatus.TOO_MANY_REQUESTS.value(), "请求过于频繁.");
            byte [] bytes = JSON.toJSONString(apiResult).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return exchange.getResponse().writeWith(Mono.just(buffer));

        });
    }
}
