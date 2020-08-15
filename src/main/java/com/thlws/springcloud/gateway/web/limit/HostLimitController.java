package com.thlws.springcloud.gateway.web.limit;

import com.thlws.commons.ApiResult;
import com.thlws.commons.data.PageResult;
import com.thlws.springcloud.gateway.internal.enums.LimiterEnum;
import com.thlws.springcloud.gateway.manage.LimitManage;
import com.thlws.springcloud.gateway.model.dto.LimitDto;
import com.thlws.springcloud.gateway.model.request.LimitRequest;
import com.thlws.springcloud.gateway.model.request.StatusRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author HanleyTang 2020/8/8
 */
@Api(tags = "Host限流")
@RestController
@CrossOrigin("*")
@RequestMapping("/limits/hosts")
public class HostLimitController {

    @Resource
    private LimitManage limitManage;

    @GetMapping("/")
    @ApiOperation(value="HOST限流列表")
    public ApiResult<PageResult<LimitDto>> list(LimitRequest request){
        PageResult<LimitDto> data = limitManage.list(request, LimiterEnum.HOST);
        return ApiResult.ok(data);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="HOST限流详情")
    public ApiResult<LimitDto> detail(@PathVariable Long id){
        LimitDto data = limitManage.detail(id, LimiterEnum.HOST);
        return ApiResult.ok(data);
    }

    @PostMapping("/")
    @ApiOperation(value="HOST限流创建")
    public ApiResult<Void> insert(@RequestBody @Valid LimitDto dto){
        limitManage.insert(dto, LimiterEnum.HOST);
        return ApiResult.ok();
    }

    @PutMapping("/")
    @ApiOperation(value="HOST限流修改")
    public ApiResult<Void> modify(@RequestBody @Valid LimitDto dto){
        limitManage.update(dto, LimiterEnum.HOST);
        return ApiResult.ok();
    }

    @PatchMapping("/status")
    @ApiOperation(value="HOST限流状态修改")
    public ApiResult<Void> modify(@RequestBody @Valid StatusRequest request){
        limitManage.updateStatus(request,LimiterEnum.HOST);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="HOST限流删除")
    public ApiResult<Void> delete(@PathVariable Long id){
        limitManage.delete(id, LimiterEnum.HOST);
        return ApiResult.ok();
    }
}
