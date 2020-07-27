package com.thlws.springcloud.gateway.internal.util;

import com.alibaba.fastjson.JSON;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author HanleyTang 2020/7/26
 */
public class RouteUtil {

    public static RouteDefinition buildRouteDefinition(Route route){
        RouteDefinition routeDefinition = new RouteDefinition();
        if (Objects.nonNull(route.getPredicates())){
            List<PredicateDefinition> predicates = JSON.parseArray(route.getPredicates(), PredicateDefinition.class);
            routeDefinition.setPredicates(predicates);
        }
        if (Objects.nonNull(route.getFilters())){
            List<FilterDefinition> filters = JSON.parseArray(route.getFilters(), FilterDefinition.class);
            routeDefinition.setFilters(filters);
        }
        routeDefinition.setUri(URI.create(route.getRouteUri()));
        routeDefinition.setId(route.getRouteId());
        routeDefinition.setOrder(route.getRouteOrder());

        return routeDefinition;
    }


    public static Route buildRoute(RouteDefinition routeDefinition) {
        Route route = new Route();
        String routeUri = routeDefinition.getUri().toString();
        route.setRouteUri(routeUri);
        route.setCreateTime(LocalDateTime.now());
        route.setRouteId(routeDefinition.getId());
        route.setRouteOrder(routeDefinition.getOrder());
        if (Objects.nonNull(routeDefinition.getFilters())) {
            route.setFilters(JSON.toJSONString(routeDefinition.getFilters()));
        }
        if (Objects.nonNull(routeDefinition.getPredicates())) {
            route.setPredicates(JSON.toJSONString(routeDefinition.getPredicates()));
        }
        return route;
    }
}
