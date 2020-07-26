package com.thlws.springcloud.gateway.internal.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.thlws.springcloud.gateway.internal.route.DynamicRouteOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author HanleyTang 2020/7/26
 */
@RestController
@RequestMapping("/routes")
public class RouteController {

    @Resource
    private DynamicRouteOperation routeOperation;

    @GetMapping("/")
    public ApiResult<IPage<Route>> routes(int pageNo,int pageSize) {
        IPage<Route> data = routeOperation.list(pageNo,pageSize);
        return ApiResult.ok(data);
    }

    @GetMapping("/{routeId}")
    public ApiResult<Route> detail(@PathVariable Long routeId) {
        Route data = routeOperation.detail(routeId);
        return ApiResult.ok(data);
    }

    @PostMapping("/")
    public ApiResult<Void> insert(@RequestBody Route entity) {
        routeOperation.insert(entity);
        return ApiResult.ok();
    }

    @PatchMapping("/")
    public ApiResult<Void> update(@RequestBody Route entity) {
        routeOperation.update(entity);
        return ApiResult.ok();
    }

    @DeleteMapping("/{routeId}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String routeId) {
        return routeOperation.delete(routeId);
    }

}
