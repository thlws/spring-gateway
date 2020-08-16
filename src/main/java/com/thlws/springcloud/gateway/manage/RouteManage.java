package com.thlws.springcloud.gateway.manage;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.commons.data.PageResult;
import com.thlws.springcloud.gateway.internal.Const;
import com.thlws.springcloud.gateway.internal.util.PageUtil;
import com.thlws.springcloud.gateway.model.dto.GatewayRouteDto;
import com.thlws.springcloud.gateway.model.request.RouteRequest;
import com.thlws.springcloud.gateway.model.request.StatusRequest;
import com.thlws.springcloud.gateway.mybatis.model.GatewayRoute;
import com.thlws.springcloud.gateway.mybatis.service.GatewayRouteService;
import io.micrometer.core.lang.NonNull;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author HanleyTang 2020/7/26
 */
@Service
public class RouteManage implements ApplicationEventPublisherAware {

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
     * @param route 路由配置
     */
    public void insert(GatewayRouteDto route) {
        gatewayRouteService.save(Convert.convert(GatewayRoute.class,route));
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void delete(Long id) {
        gatewayRouteService.removeById(id);
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(GatewayRouteDto route) {
        gatewayRouteService.updateById(Convert.convert(GatewayRoute.class,route));
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void updateStatus(StatusRequest request) {
        GatewayRoute gatewayRoute = gatewayRouteService.getById(request.getId());
        gatewayRoute.setStatus(request.getStatus());
        gatewayRouteService.updateById(gatewayRoute);
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public GatewayRouteDto detail(Long id){
        return Convert.convert(GatewayRouteDto.class, gatewayRouteService.getById(id));
    }

    public PageResult<GatewayRouteDto> list(RouteRequest request){
        PageUtil.build(request);
        LambdaQueryWrapper<GatewayRoute> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Objects.nonNull(request.getStatus()), GatewayRoute::getStatus, request.getStatus());
        queryWrapper.like(Objects.nonNull(request.getPredicatePath()), GatewayRoute::getPredicatePath, request.getPredicatePath());
        queryWrapper.like(Objects.nonNull(request.getRouteId()), GatewayRoute::getRouteId, request.getRouteId());

        if (StrUtil.equals(Const.Sort.ID_DESC, request.getSort())) {
            queryWrapper.orderByDesc(GatewayRoute::getId);
        }

        Page<GatewayRoute> routePage = gatewayRouteService.page(new Page<>(request.getPage(), request.getSize()),queryWrapper);
        long total = routePage.getTotal();
        List<GatewayRoute> originRecords = routePage.getRecords();
        List<GatewayRouteDto> records = Convert.toList(GatewayRouteDto.class, originRecords);
        return new PageResult<>(records,total);
    }
}
