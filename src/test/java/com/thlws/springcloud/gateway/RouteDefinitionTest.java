package com.thlws.springcloud.gateway;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

/**
 * @author HanleyTang 2020/8/3
 */
public class RouteDefinitionTest {

    public static void main(String[] args) {
        FilterDefinition filterDefinition = new FilterDefinition("StripPrefix=2");
        PredicateDefinition predicateDefinition = new PredicateDefinition("Path=/api/user/**");
        PredicateDefinition predicateDefinition2 = new PredicateDefinition("Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]");
//        List<PredicateDefinition> predicateDefinitions =  routeDefinition.getPredicates();
//        List<FilterDefinition>  filterDefinitions = routeDefinition.getFilters();
        System.out.println(filterDefinition);
        System.out.println(predicateDefinition);

    }
}
