package com.thlws.springcloud.gateway.internal.core.service;

import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 路由配置 服务类
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-07-27
 */
public interface RouteService extends IService<Route> {

    public void deleteByRouteId(String routeId);
}
