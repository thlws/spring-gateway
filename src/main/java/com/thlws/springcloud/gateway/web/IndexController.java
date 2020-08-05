package com.thlws.springcloud.gateway.web;

import com.thlws.springcloud.gateway.internal.util.PathUtil;
import com.thlws.springcloud.gateway.limiter.config.LimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
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

   @Resource(name = "reactiveRedisTemplate")
   private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;


    @RequestMapping("/db")
    public String index(){
        return username;
    }

    @RequestMapping("/host/add")
    public Mono<Boolean> host(){
        LimiterConfig config = LimiterConfig.builder()
                .burstCapacity(1)
                .replenishRate(1)
                .requestedTokens(1).build();
        Map<String, Object> map = new HashMap<>(2);
        map.put("localhost", config);
        map.put("127.0.0.1", config);
        return reactiveRedisTemplate.opsForHash().putAll("limiter:config:host",map);

    }

    @GetMapping("/host/get/{host}")
    public Mono<LimiterConfig> get(@PathVariable String host){
        LimiterConfig defaultConfig = LimiterConfig.builder().requestedTokens(-1).replenishRate(-1).burstCapacity(-1).build();
        Mono<Object> limiter = reactiveRedisTemplate.opsForHash().get("limiter:config:host", host);
        return limiter.flatMap(e->{
            LimiterConfig config = (LimiterConfig)e;
            log.info("gateway host limiter host=[{}],config=[{}]",host,config.toString());
            return Mono.just(config);
        }).switchIfEmpty(Mono.just(defaultConfig));

    }

    @GetMapping("/api/values")
    public Mono<LimiterConfig> values(){
        LimiterConfig defaultConfig = LimiterConfig.builder().requestedTokens(-1).replenishRate(-1).burstCapacity(-1).build();
        Flux<Object> limiter =reactiveRedisTemplate.opsForHash().values("limiter:config:api");
        Flux<Object> matchedList = limiter.filter(e-> PathUtil.match("/api/user/{name}","/api/user/ha/flow"));
        Mono<Object> matchedOne = matchedList.singleOrEmpty();

        return matchedOne.flatMap(e->{
            LimiterConfig config = (LimiterConfig)e;
            return Mono.just(config);
        }).switchIfEmpty(Mono.just(defaultConfig));

    }

    @RequestMapping("/api/add")
    public Mono<Boolean> api(){
        LimiterConfig config = LimiterConfig.builder()
                .burstCapacity(1)
                .replenishRate(1)
                .requestedTokens(1).build();
        Map<String, Object> map = new HashMap<>(1);
        map.put("/api/user/{name}", config);
        return reactiveRedisTemplate.opsForHash().putAll("limiter:config:api",map);

    }

    @RequestMapping("/api/ms")
    public Flux<Object> members(){
        Flux<Object> limiter = reactiveRedisTemplate.opsForHash().keys("limiter:config:api");
        return limiter;
    }


}
