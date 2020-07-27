package com.thlws.springcloud.gateway.internal.route;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HanleyTang 2020/7/27
 */
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    public static final String   GATEWAY_ROUTES = "gateway:routes";

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return null;
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
