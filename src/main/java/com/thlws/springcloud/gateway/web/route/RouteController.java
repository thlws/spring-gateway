package com.thlws.springcloud.gateway.web.route;

import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.data.PageResult;
import com.thlws.springcloud.gateway.internal.route.DynamicRouteOperation;
import com.thlws.springcloud.gateway.model.dto.GatewayRouteDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author HanleyTang 2020/7/26
 */
@Api(tags = "路由管理")
@RestController
@RequestMapping("/routes")
public class RouteController {

    @Resource
    private DynamicRouteOperation routeOperation;

    @GetMapping("/refresh")
    @ApiOperation(value="刷新路由")
    public ApiResult<Void> routes() {
        routeOperation.refreshRoutes();
        return ApiResult.ok();
    }

    @GetMapping("/")
    @ApiOperation(value="路由列表")
    public ApiResult<PageResult<GatewayRouteDto>> routes(int page, int size) {
        PageResult<GatewayRouteDto> data = routeOperation.list(page,size);
        return ApiResult.ok(data);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="路由详情")
    public ApiResult<GatewayRouteDto> detail(@PathVariable Long id) {
        GatewayRouteDto data = routeOperation.detail(id);
        return ApiResult.ok(data);
    }

    @PostMapping("/")
    @ApiOperation(value="路由新增")
    public ApiResult<Void> insert(@RequestBody GatewayRouteDto entity) {
        routeOperation.insert(entity);
        return ApiResult.ok();
    }

    @PatchMapping("/")
    @ApiOperation(value="路由更新")
    public ApiResult<Void> update(@RequestBody GatewayRouteDto entity) {
        routeOperation.update(entity);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="路由删除")
    public  ApiResult<Void> delete(@PathVariable Long id) {
        routeOperation.delete(id);
        return ApiResult.ok();
    }

}
