package com.thlws.springcloud.gateway.internal.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.thlws.springcloud.gateway.internal.core.model.GatewayRoute;
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




    public static RouteDefinition buildGatewayRouteDefinition(GatewayRoute route){
        RouteDefinition routeDefinition = new RouteDefinition();
        String predicatePath = route.getPredicatePath();
        if (Objects.nonNull(predicatePath)){
            if (!predicatePath.startsWith("/")) {
                predicatePath = "/"+predicatePath;
            }
            if (!predicatePath.endsWith("/**")) {
                predicatePath = predicatePath + "/**";
            }

            PredicateDefinition predicateDefinition = new PredicateDefinition("Path="+predicatePath);
            routeDefinition.getPredicates().add(predicateDefinition);
            int count = StrUtil.count(predicatePath, "/");
            //StrUtil.count(predicatePath, "/api/user/**");

            FilterDefinition filterDefinition = new FilterDefinition("StripPrefix="+count);
            routeDefinition.getFilters().add(filterDefinition);

        }
        routeDefinition.setUri(URI.create(route.getRouteUri()));
        routeDefinition.setId(route.getRouteId());

        return routeDefinition;
    }





}
