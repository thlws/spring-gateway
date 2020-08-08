package com.thlws.springcloud.gateway.internal.route;

import com.google.common.collect.Lists;
import com.thlws.springcloud.gateway.mybatis.model.GatewayRoute;
import com.thlws.springcloud.gateway.mybatis.service.GatewayRouteService;
import com.thlws.springcloud.gateway.internal.util.RouteUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * save,delete 可不在这里实现,基于普通Service操作数据,效果一样，
 * 但要触发刷新动作,会自动调用 getRouteDefinitions,将最新数据更新到网关
 * @see DynamicRouteOperation#refreshRoutes()
 * @see MySqlRouteDefinitionRepository#getRouteDefinitions()
 * @author HanleyTang 2020/7/26
 */
@Component
public class MySqlRouteDefinitionRepository implements RouteDefinitionRepository {

    @Resource
    private GatewayRouteService gatewayRouteService;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = Lists.newArrayList();
        List<GatewayRoute> routes = gatewayRouteService.list();
        routes.forEach(e-> routeDefinitions.add(RouteUtil.buildGatewayRouteDefinition(e)));
        return Flux.fromIterable(routeDefinitions);
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
