package com.thlws.springcloud.gateway.web.route;

import com.thlws.commons.ApiResult;
import com.thlws.commons.data.PageResult;
import com.thlws.springcloud.gateway.manage.RouteManage;
import com.thlws.springcloud.gateway.model.dto.GatewayRouteDto;
import com.thlws.springcloud.gateway.model.request.RouteRequest;
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
    private RouteManage routeManage;

    @GetMapping("/refresh")
    @ApiOperation(value="刷新路由")
    public ApiResult<Void> routes() {
        routeManage.refreshRoutes();
        return ApiResult.ok();
    }

    @GetMapping("/")
    @ApiOperation(value="路由列表")
    public ApiResult<PageResult<GatewayRouteDto>> routes(RouteRequest request) {
        PageResult<GatewayRouteDto> data = routeManage.list(request);
        return ApiResult.ok(data);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="路由详情")
    public ApiResult<GatewayRouteDto> detail(@PathVariable Long id) {
        GatewayRouteDto data = routeManage.detail(id);
        return ApiResult.ok(data);
    }

    @PostMapping("/")
    @ApiOperation(value="路由新增")
    public ApiResult<Void> insert(@RequestBody GatewayRouteDto entity) {
        routeManage.insert(entity);
        return ApiResult.ok();
    }

    @PatchMapping("/")
    @ApiOperation(value="路由更新")
    public ApiResult<Void> update(@RequestBody GatewayRouteDto entity) {
        routeManage.update(entity);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="路由删除")
    public  ApiResult<Void> delete(@PathVariable Long id) {
        routeManage.delete(id);
        return ApiResult.ok();
    }

}
