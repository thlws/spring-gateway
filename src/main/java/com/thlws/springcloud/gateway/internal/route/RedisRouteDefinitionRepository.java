package com.thlws.springcloud.gateway.internal.route;

import com.alibaba.fastjson.JSON;
import com.thlws.springcloud.gateway.internal.core.model.GatewayRoute;
import com.thlws.springcloud.gateway.internal.util.RouteUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 未启用
 * @author HanleyTang 2020/7/27
 */
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    public static final String GATEWAY_ROUTES = "gateway:routes";

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        Flux<String> routeFlux = reactiveStringRedisTemplate.opsForSet().members(GATEWAY_ROUTES);
        return routeFlux.map(e->{
            GatewayRoute gatewayRoute = JSON.parseObject(e, GatewayRoute.class);
            return RouteUtil.buildGatewayRouteDefinition(gatewayRoute);
        }).switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.empty();
    }
}
