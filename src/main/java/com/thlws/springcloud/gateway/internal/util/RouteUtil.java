package com.thlws.springcloud.gateway.internal.util;

import cn.hutool.core.util.StrUtil;
import com.thlws.springcloud.gateway.mybatis.model.GatewayRoute;
import com.thlws.springcloud.gateway.internal.enums.StripTypeEnum;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.net.URI;
import java.util.Objects;

/**
 * @author HanleyTang 2020/7/26
 */
public class RouteUtil {

    private final static String SLASH = "/";
    private final static String PATH_TAIL = "/**";

    public static RouteDefinition buildGatewayRouteDefinition(GatewayRoute route){
        RouteDefinition routeDefinition = new RouteDefinition();
        String predicatePath = route.getPredicatePath();
        if (Objects.nonNull(predicatePath)){
            if (!predicatePath.startsWith(SLASH)) {
                predicatePath = SLASH +predicatePath;
            }
            if (!predicatePath.endsWith(PATH_TAIL)) {
                predicatePath = predicatePath + PATH_TAIL;
            }

            PredicateDefinition predicateDefinition = new PredicateDefinition("Path="+predicatePath);
            routeDefinition.getPredicates().add(predicateDefinition);
            int stripSize = StrUtil.count(predicatePath, SLASH) -1;
            stripSize = Math.max(stripSize, 0);

            if (route.getStripType() == StripTypeEnum.TRUE.value()) {
                FilterDefinition filterDefinition = new FilterDefinition("StripPrefix="+stripSize);
                routeDefinition.getFilters().add(filterDefinition);
            }
        }
        routeDefinition.setUri(URI.create(route.getRouteUri()));
        routeDefinition.setId(route.getRouteId());

        return routeDefinition;
    }





}
