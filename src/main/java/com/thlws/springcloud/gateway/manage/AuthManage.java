package com.thlws.springcloud.gateway.manage;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.commons.data.PageResult;
import com.thlws.springcloud.gateway.auth.config.AuthConfig;
import com.thlws.springcloud.gateway.internal.Const;
import com.thlws.springcloud.gateway.internal.enums.AuthEnum;
import com.thlws.springcloud.gateway.internal.util.PageUtil;
import com.thlws.springcloud.gateway.model.dto.ApiAuthDto;
import com.thlws.springcloud.gateway.model.request.AuthRequest;
import com.thlws.springcloud.gateway.model.request.AuthStatusRequest;
import com.thlws.springcloud.gateway.mybatis.model.ApiAuth;
import com.thlws.springcloud.gateway.mybatis.service.ApiAuthService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
        dto.setCreateTime(LocalDateTime.now());
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

        ApiAuth apiAuth = authService.getById(request.getId());
        apiAuth.setAuth(request.getAuth());
        authService.updateById(apiAuth);

        ApiAuthDto newDto = Convert.convert(ApiAuthDto.class, apiAuth);

        if (request.getAuth() == AuthEnum.AUTH.value()) {
            syncToRedis(newDto);
        }else{
            removeFromRedis(newDto);
        }
    }

    public PageResult<ApiAuthDto> list(AuthRequest request){

        PageUtil.build(request);
        LambdaQueryWrapper<ApiAuth> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Objects.nonNull(request.getAuth()), ApiAuth::getAuth, request.getAuth());
        queryWrapper.like(Objects.nonNull(request.getPath()), ApiAuth::getPath, request.getPath());
        queryWrapper.like(Objects.nonNull(request.getAuthHttpMethod()), ApiAuth::getAuthHttpMethod, request.getAuthHttpMethod());
        if (StrUtil.equals(Const.Sort.ID_DESC, request.getSort())) {
            queryWrapper.orderByDesc(ApiAuth::getId);
        }

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
