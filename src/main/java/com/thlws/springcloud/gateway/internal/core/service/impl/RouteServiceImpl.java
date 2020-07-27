package com.thlws.springcloud.gateway.internal.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thlws.springcloud.gateway.internal.core.mapper.RouteMapper;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.thlws.springcloud.gateway.internal.core.service.RouteService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 路由配置 服务实现类
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-07-27
 */
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements RouteService {

    @Override
    public void deleteByRouteId(String routeId) {
        LambdaQueryWrapper<Route> lambdaQueryWrapper =  Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Route::getRouteId, routeId);
        remove(lambdaQueryWrapper);
    }
}
