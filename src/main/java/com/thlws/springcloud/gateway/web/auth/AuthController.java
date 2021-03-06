package com.thlws.springcloud.gateway.web.auth;

import com.thlws.commons.ApiResult;
import com.thlws.commons.data.PageResult;
import com.thlws.springcloud.gateway.manage.AuthManage;
import com.thlws.springcloud.gateway.model.dto.ApiAuthDto;
import com.thlws.springcloud.gateway.model.request.AuthRequest;
import com.thlws.springcloud.gateway.model.request.AuthStatusRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author HanleyTang 2020/8/9
 */
@Api(tags = "鉴权管理")
@RestController
@CrossOrigin("*")
@RequestMapping("/auths")
public class AuthController {

    @Resource
    private AuthManage authManage;

    @GetMapping("/")
    @ApiOperation(value="鉴权API列表")
    public ApiResult<PageResult<ApiAuthDto>> list(AuthRequest request){
        PageResult<ApiAuthDto> data = authManage.list(request);
        return ApiResult.ok(data);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="鉴权API详情")
    public ApiResult<ApiAuthDto> detail(@PathVariable Long id){
        ApiAuthDto data = authManage.detail(id);
        return ApiResult.ok(data);
    }

    @PostMapping("/")
    @ApiOperation(value="鉴权API创建")
    public ApiResult<Void> insert(@RequestBody @Valid ApiAuthDto dto){
        authManage.insert(dto);
        return ApiResult.ok();
    }

    @PutMapping("/")
    @ApiOperation(value="鉴权API修改")
    public ApiResult<Void> modify(@RequestBody @Valid ApiAuthDto dto){
        authManage.update(dto);
        return ApiResult.ok();
    }

    @PatchMapping("/status")
    @ApiOperation(value="鉴权API状态修改")
    public ApiResult<Void> modify(@RequestBody @Valid AuthStatusRequest request){
        authManage.updateStatus(request);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="鉴权API删除")
    public ApiResult<Void> delete(@PathVariable Long id){
        authManage.delete(id);
        return ApiResult.ok();
    }
}
