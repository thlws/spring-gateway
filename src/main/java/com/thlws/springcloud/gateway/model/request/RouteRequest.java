package com.thlws.springcloud.gateway.model.request;

import com.thlws.commons.data.PageRequest;

/**
 * @author HanleyTang 2020/8/9
 */
public class RouteRequest extends PageRequest {

    /**
     * 路由编号,一般对应一个微服务
     */
    private String routeId;


    /**
     * 1内部服务; 2外部地址
     */
    private Integer routeType;

    /**
     * 匹配路径,eg:/api/user/**
     */
    private String predicatePath;

    /**
     * 0禁用; 1启用
     */
    private Integer status;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Integer getRouteType() {
        return routeType;
    }

    public void setRouteType(Integer routeType) {
        this.routeType = routeType;
    }

    public String getPredicatePath() {
        return predicatePath;
    }

    public void setPredicatePath(String predicatePath) {
        this.predicatePath = predicatePath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
