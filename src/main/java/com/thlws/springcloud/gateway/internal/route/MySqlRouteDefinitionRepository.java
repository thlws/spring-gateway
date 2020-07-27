package com.thlws.springcloud.gateway.internal.route;

import com.google.common.collect.Lists;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.thlws.springcloud.gateway.internal.core.service.RouteService;
import com.thlws.springcloud.gateway.internal.util.RouteUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HanleyTang 2020/7/26
 */
@Component
public class MySqlRouteDefinitionRepository implements RouteDefinitionRepository {

    @Resource
    private RouteService routeService;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = Lists.newArrayList();
        List<Route> routes = routeService.list();
        routes.forEach(r-> routeDefinitions.add(RouteUtil.buildRouteDefinition(r)));
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            routeService.save(RouteUtil.buildRoute(routeDefinition));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            routeService.deleteByRouteId(id);
            return Mono.empty();
        });
    }


}
