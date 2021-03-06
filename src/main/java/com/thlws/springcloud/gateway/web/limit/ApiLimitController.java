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
@Api(tags = "API限流")
@RestController
@CrossOrigin("*")
@RequestMapping("/limits/apis")
public class ApiLimitController {

    @Resource
    private LimitManage limitManage;

    @GetMapping("/")
    @ApiOperation(value="API限流列表")
    public ApiResult<PageResult<LimitDto>> list(LimitRequest request){
        PageResult<LimitDto> data = limitManage.list(request, LimiterEnum.API);
        return ApiResult.ok(data);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="API限流详情")
    public ApiResult<LimitDto> detail(@PathVariable Long id){
        LimitDto data = limitManage.detail(id,LimiterEnum.API);
        return ApiResult.ok(data);
    }

    @PostMapping("/")
    @ApiOperation(value="API限流创建")
    public ApiResult<Void> insert(@RequestBody @Valid LimitDto dto){
        limitManage.insert(dto, LimiterEnum.API);
        return ApiResult.ok();
    }

    @PutMapping("/")
    @ApiOperation(value="API限流修改")
    public ApiResult<Void> modify(@RequestBody @Valid LimitDto dto){
        limitManage.update(dto,LimiterEnum.API);
        return ApiResult.ok();
    }

    @PatchMapping("/status")
    @ApiOperation(value="API限流状态修改")
    public ApiResult<Void> modifyStatus(@RequestBody @Valid StatusRequest request){
        limitManage.updateStatus(request,LimiterEnum.API);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="API限流删除")
    public ApiResult<Void> delete(@PathVariable Long id){
        limitManage.delete(id,LimiterEnum.API);
        return ApiResult.ok();
    }
}
