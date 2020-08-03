package com.thlws.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Gateway 内置限流配置
 * @author HanleyTang 2020/8/1
 */
@Configuration
public class RateLimiterConfiguration {

    @Bean("userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getQueryParams().getFirst("userId")));
    }

    @Bean("hostAddrKeyResolver")
    public KeyResolver hostAddrKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
    }

    @Primary
    @Bean("apiKeyResolver")
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }



//    @Bean
//    public GlobalFilter customGlobalFilter() {
//        return (exchange, chain) -> {
//            String path = exchange.getRequest().getPath().value()
//                    .map(Principal::getName)
//                    .defaultIfEmpty("Default User")
//                    .map(userName -> {
//                        //adds header to proxied request
//                        exchange.getRequest().mutate().header("CUSTOM-REQUEST-HEADER", userName).build();
//                        return exchange;
//                    })
//                    .flatMap(chain::filter);
//        }
//    }
//
//
//    @Bean
//    public GlobalFilter customGlobalPostFilter() {
//        return (exchange, chain) -> chain.filter(exchange)
//                .then(Mono.just(exchange))
//                .map(serverWebExchange -> {
//                    //adds header to response
////                    ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
//                    if (Objects.equals(serverWebExchange.getResponse().getStatusCode(), HttpStatus.TOO_MANY_REQUESTS)) {
//                        ApiResult<String> apiResult = ApiResult.error(500, "请求过于频繁.");
//                        byte [] bytes = JSON.toJSONString(apiResult).getBytes(StandardCharsets.UTF_8);
//                        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
//                        serverWebExchange.getResponse().writeWith(Mono.just(buffer));
//                    }
//                    return serverWebExchange;
//                })
//                .then();
//    }

}
