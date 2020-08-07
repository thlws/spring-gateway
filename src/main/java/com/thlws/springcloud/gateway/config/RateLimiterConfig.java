package com.thlws.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Gateway 内置限流配置，主要用于微服务级别限流,缺点是被限流后没有body返回
 * 只有HttpStatusCode=429,暂时没能解决官方被限流后返回body自定义。
 * @author HanleyTang 2020/8/1
 */
@Configuration
public class RateLimiterConfig {

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

}
