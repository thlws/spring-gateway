package com.thlws.springcloud.gateway.internal.route;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.springcloud.gateway.internal.core.model.GatewayRoute;
import com.thlws.springcloud.gateway.internal.core.service.GatewayRouteService;
import com.thlws.springcloud.gateway.internal.core.service.RouteService;
import io.micrometer.core.lang.NonNull;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HanleyTang 2020/7/26
 */
@Service
public class DynamicRouteOperation implements ApplicationEventPublisherAware {

    @Resource
    private GatewayRouteService gatewayRouteService;

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
    public void insertGatewayRoute(GatewayRoute route) {
        gatewayRouteService.save(route);
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void deleteGatewayRoute(Long id) {
        gatewayRouteService.removeById(id);
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void updateGatewayRoute(GatewayRoute route) {
        gatewayRouteService.updateById(route);
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public GatewayRoute detailGatewayRoute(Long id){
        return gatewayRouteService.getById(id);
    }

    public IPage<GatewayRoute> listGatewayRoute(int pageNo,int pageSize){
        return gatewayRouteService.page(new Page<>(pageNo, pageSize));
    }
}
