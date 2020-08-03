package com.thlws.springcloud.gateway.limiter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;

/**
 * Spring Cloud Gateway 自定义限流
 * @author HanleyTang
 * @see org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter
 * @see org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory
 */
@Slf4j
@Component
public class MyRateLimiter {

    private final ReactiveStringRedisTemplate redisTemplate;

    private final RedisScript<List<Long>> redisScript;

    /**
     * Instantiates a new My redis rate limiter.
     *
     * @param redisTemplate The reactiveStringRedisTemplate
     * @param redisScript   注入 gateway 官方 RedisScript  request-rate-limiter.lua 脚本
     */
    public MyRateLimiter(ReactiveStringRedisTemplate redisTemplate,
                         RedisScript<List<Long>> redisScript) {
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    /***
     * 放行检测
     * @param key 限流纬度key, 可以是 API, HOST, UER
     * @param config 限流配置
     * @return 是否放行
     */
    public Mono<RateLimiter.Response> isAllowed(String key, LimiterConfig config) {

        List<String> keys = getKeys(key);

        try {
            // 一秒钟的请求次数，即qps，就是往令牌桶放的速度
            // How many requests per second do you want a user to be allowed to do?
            int replenishRate = config.getReplenishRate();

            // 令牌桶有多大
            // How much bursting do you want to allow?
            int burstCapacity = config.getBurstCapacity();

            // 每个请求请求多少个令牌,默认1
            // How many tokens are requested per request?
            int requestedTokens = config.getRequestedTokens();

            List<String> scriptArgs = Arrays.asList(replenishRate + "",
                    burstCapacity + "",
                    Instant.now().getEpochSecond() + "",
                    requestedTokens + "");

            // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
            Flux<List<Long>> flux = this.redisTemplate.execute(this.redisScript, keys,
                    scriptArgs);

            return flux.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                    .reduce(new ArrayList<Long>(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }).map(results -> {
                        boolean allowed = results.get(0) == 1L;
                        Long tokensLeft = results.get(1);

                        RateLimiter.Response response = new RateLimiter.Response(allowed,
                                getHeaders(config, tokensLeft));

                        if (log.isDebugEnabled()) {
                            log.debug("response: " + response);
                        }
                        return response;
                    });


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Mono.just(new RateLimiter.Response(true, getHeaders(config, -1L)));

    }


    private static List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "gateway:custom:request:rate:limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    private Map<String, String> getHeaders(LimiterConfig config, Long tokensLeft) {
        Map<String, String> headers = new HashMap<>(Collections.emptyMap());
            headers.put(RedisRateLimiter.REMAINING_HEADER, tokensLeft.toString());
            headers.put(RedisRateLimiter.REPLENISH_RATE_HEADER,
                    String.valueOf(config.getReplenishRate()));
            headers.put(RedisRateLimiter.BURST_CAPACITY_HEADER,
                    String.valueOf(config.getBurstCapacity()));
            headers.put(RedisRateLimiter.REQUESTED_TOKENS_HEADER,
                    String.valueOf(config.getRequestedTokens()));
        return headers;
    }

}