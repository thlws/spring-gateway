package com.thlws.springcloud.gateway.manage;

import cn.hutool.core.convert.Convert;
import com.thlws.commons.data.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.springcloud.gateway.auth.config.AuthConfig;
import com.thlws.springcloud.gateway.internal.Const;
import com.thlws.springcloud.gateway.internal.enums.AuthEnum;
import com.thlws.springcloud.gateway.model.dto.ApiAuthDto;
import com.thlws.springcloud.gateway.model.request.AuthRequest;
import com.thlws.springcloud.gateway.model.request.AuthStatusRequest;
import com.thlws.springcloud.gateway.mybatis.model.ApiAuth;
import com.thlws.springcloud.gateway.mybatis.service.ApiAuthService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author HanleyTang 2020/8/9
 */
@Component
public class AuthManage {

    @Resource
    private ApiAuthService authService;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public void insert(ApiAuthDto dto){
        authService.save(Convert.convert(ApiAuth.class, dto));
        syncToRedis(dto);
    }

    public void update(ApiAuthDto dto){
        authService.updateById(Convert.convert(ApiAuth.class, dto));
    }

    public ApiAuthDto detail(Long id){
        ApiAuth auth = authService.getById(id);
        return Convert.convert(ApiAuthDto.class, auth);
    }

    public void delete(Long id){
        ApiAuthDto dto = detail(id);
        authService.removeById(id);
        syncToRedis(dto);
    }

    public void updateStatus(AuthStatusRequest request) {
        LambdaUpdateWrapper<ApiAuth> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ApiAuth::getId, request.getId());
        updateWrapper.eq(ApiAuth::getAuth, request.getAuth());
        authService.update(updateWrapper);
        ApiAuthDto newDto = detail(request.getId());
        if (request.getAuth() == AuthEnum.AUTH.value()) {
            syncToRedis(newDto);
        }else{
            removeFromRedis(newDto);
        }
    }

    public PageResult<ApiAuthDto> list(AuthRequest request){

        ApiAuthDto dto = request.getData();
        LambdaQueryWrapper<ApiAuth> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Objects.nonNull(dto.getAuth()), ApiAuth::getAuth, dto.getAuth());
        queryWrapper.like(Objects.nonNull(dto.getPath()), ApiAuth::getPath, dto.getPath());
        queryWrapper.like(Objects.nonNull(dto.getAuthHttpMethod()), ApiAuth::getAuthHttpMethod, dto.getAuthHttpMethod());

        IPage<ApiAuth> pageData = authService.page(new Page<>(request.getPage(), request.getSize()),queryWrapper);
        List<ApiAuth> records = pageData.getRecords();
        long total = pageData.getTotal();
        List<ApiAuthDto> newRecords = Convert.toList(ApiAuthDto.class, records);
        return new PageResult<>(newRecords,total);

    }


    private void syncToRedis(ApiAuthDto dto){
        if (dto.getAuth() == AuthEnum.AUTH.value()) {
            AuthConfig config = AuthConfig.builder()
                    .path(dto.getPath())
                    .authHttpMethod(dto.getAuthHttpMethod()).build();
            reactiveRedisTemplate.opsForSet().add(Const.Key.AUTH_API,config);
        }
    }

    private void removeFromRedis(ApiAuthDto dto){
        AuthConfig config = AuthConfig.builder()
                .path(dto.getPath())
                .authHttpMethod(dto.getAuthHttpMethod()).build();
        reactiveRedisTemplate.opsForSet().remove(Const.Key.AUTH_API, config);

    }

}
