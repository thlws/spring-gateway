package com.thlws.springcloud.gateway.internal.route;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.thlws.springcloud.gateway.internal.core.service.RouteService;
import com.thlws.springcloud.gateway.internal.util.RouteUtil;
import io.micrometer.core.lang.NonNull;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author HanleyTang 2020/7/26
 */
@Service
public class DynamicRouteOperation implements ApplicationEventPublisherAware {

    @Resource
    private RouteService routeService;

    @Resource(name = "mySqlRouteDefinitionRepository")
    private RouteDefinitionRepository routeDefinitionRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void refreshRoutes() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 新增网关.
     * 直接操作 routeService 完成数据插入并刷新,效果一样
     * @see RouteService#save(Object)
     * @param route 路由配置
     */
    public void insert(Route route) {
        RouteDefinition routeDefinition = RouteUtil.buildRouteDefinition(route);
        routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(Route route) {
        routeService.updateById(route);
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 移除路由.
     * 直接操作 routeService 删除数据,效果一样
     * @see RouteService#removeById(Serializable)
     * @param id the id
     */
    public void delete(Long id) {
        Route route = detail(id);
        routeDefinitionRepository.delete(Mono.just(route.getRouteId())).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public IPage<Route> list(int pageNo,int pageSize){
        return routeService.page(new Page<>(pageNo, pageSize));
    }

    public Route detail(Long id){
        return routeService.getById(id);
    }
}
