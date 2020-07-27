package com.thlws.springcloud.gateway.internal.route;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.thlws.springcloud.gateway.internal.core.service.RouteService;
import com.thlws.springcloud.gateway.internal.util.RouteUtil;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author HanleyTang 2020/7/26
 */
@Service
public class DynamicRouteOperation implements ApplicationEventPublisherAware {

    @Resource
    private RouteService routeService;

    @Resource(name = "mySqlRouteDefinitionRepository")
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void refreshRoutes() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void insert(Route route) {
        RouteDefinition routeDefinition = RouteUtil.buildRouteDefinition(route);
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(Route route) {
        RouteDefinition routeDefinition = RouteUtil.buildRouteDefinition(route);
        routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void delete(Long id) {
        Route route = detail(id);
        routeDefinitionWriter.delete(Mono.just(route.getRouteId())).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public IPage<Route> list(int pageNo,int pageSize){
        return routeService.page(new Page<>(pageNo, pageSize));
    }

    public Route detail(Long id){
        return routeService.getById(id);
    }
}
