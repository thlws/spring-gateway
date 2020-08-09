package com.thlws.springcloud.gateway.web;

import com.thlws.springcloud.gateway.auth.config.AuthConfig;
import com.thlws.springcloud.gateway.internal.Const;
import com.thlws.springcloud.gateway.limiter.config.LimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HanleyTang 2020/7/27
 */
@Slf4j
@RestController
public class IndexController {

    @Value("${db.user.username}")
    private String username;

   @Resource(name = "customerReactiveRedisTemplate")
   private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @RequestMapping("/host/add")
    public Mono<Boolean> hostAdd(){
        LimiterConfig config = LimiterConfig.builder()
                .burstCapacity(1)
                .replenishRate(1)
                .limitHttpMethod("GET,POST")
                .requestedTokens(1).build();
        Map<String, Object> map = new HashMap<>(2);
        map.put("localhost", config);
        map.put("127.0.0.1", config);
        return reactiveRedisTemplate.opsForHash().putAll("limiter:config:host",map);

    }


    @RequestMapping("/api/add")
    public Mono<Boolean> apiAdd(){
        LimiterConfig config = LimiterConfig.builder()
                .burstCapacity(1)
                .replenishRate(1)
                .limitHttpMethod("GET")
                .requestedTokens(1).build();
        Map<String, Object> map = new HashMap<>(1);
        map.put("/api/user/{name}", config);
        return reactiveRedisTemplate.opsForHash().putAll("limiter:config:api",map);

    }



    @RequestMapping("/user/add")
    public Mono<Boolean> userAdd(){
        LimiterConfig config = LimiterConfig.builder()
                .burstCapacity(1)
                .replenishRate(1)
                .limitHttpMethod("*")
                .requestedTokens(1).build();
        Map<String, Object> map = new HashMap<>(1);
        map.put("hanley", config);
        return reactiveRedisTemplate.opsForHash().putAll("limiter:config:user",map);

    }


    @RequestMapping("/auth/add")
    public Mono<Long> authAdd(){
        AuthConfig config = AuthConfig.builder()
                .path("/api/user/{name}")
                .authHttpMethod("GET")
                .build();
        return reactiveRedisTemplate.opsForSet().add(Const.Key.AUTH_API,config);

    }

}
