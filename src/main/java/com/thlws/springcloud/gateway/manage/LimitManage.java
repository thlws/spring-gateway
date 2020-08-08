package com.thlws.springcloud.gateway.manage;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.springcloud.gateway.data.PageResult;
import com.thlws.springcloud.gateway.internal.enums.LimiterEnum;
import com.thlws.springcloud.gateway.internal.enums.StatusEnum;
import com.thlws.springcloud.gateway.limiter.config.LimiterConfig;
import com.thlws.springcloud.gateway.model.dto.LimitDto;
import com.thlws.springcloud.gateway.model.request.LimitRequest;
import com.thlws.springcloud.gateway.model.request.StatusRequest;
import com.thlws.springcloud.gateway.mybatis.model.GatewayLimit;
import com.thlws.springcloud.gateway.mybatis.service.GatewayLimitService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author HanleyTang 2020/8/8
 */
@Service
public class LimitManage {

    @Resource
    private GatewayLimitService limitService;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public PageResult<LimitDto> list(LimitRequest request,LimiterEnum limiterEnum){

        LimitDto dto = request.getData();
        LambdaQueryWrapper<GatewayLimit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GatewayLimit::getLimitType, limiterEnum.value());
        queryWrapper.eq(Objects.nonNull(dto.getStatus()), GatewayLimit::getStatus, dto.getStatus());
        queryWrapper.eq(Objects.nonNull(dto.getLimitValue()), GatewayLimit::getLimitValue, dto.getLimitValue());
        queryWrapper.like(Objects.nonNull(dto.getLimitContent()), GatewayLimit::getLimitContent, dto.getLimitContent());
        queryWrapper.like(Objects.nonNull(dto.getLimitHttpMethod()), GatewayLimit::getLimitHttpMethod, dto.getLimitHttpMethod());

        IPage<GatewayLimit> pageData = limitService.page(new Page<>(request.getPage(), request.getSize()),queryWrapper);
        List<GatewayLimit> records = pageData.getRecords();
        long total = pageData.getTotal();
        List<LimitDto> newRecords = Convert.toList(LimitDto.class, records);
        return new PageResult<>(newRecords,total);

    }

    public LimitDto detail(Long id,LimiterEnum limiterEnum) {
        GatewayLimit entity = limitService.getById(id);
        return Convert.convert(LimitDto.class, entity);
    }

    public void insert(LimitDto dto ,LimiterEnum limiterEnum) {
        GatewayLimit entity = Convert.convert(GatewayLimit.class, dto);
        entity.setLimitType(limiterEnum.value());
        limitService.save(entity);
        syncToRedis(limiterEnum, dto);
    }

    public void update(LimitDto dto,LimiterEnum limiterEnum) {
        GatewayLimit entity = Convert.convert(GatewayLimit.class, dto);
        limitService.updateById(entity);
        if (dto.getStatus() == StatusEnum.ENABLE.value()) {
            LimitDto newDto = detail(entity.getId(),limiterEnum);
            syncToRedis(limiterEnum, newDto);
        }else{
            removeFromRedis(limiterEnum, dto.getLimitContent());
        }
    }

    public void delete(Long id,LimiterEnum limiterEnum) {
        LimitDto dto = detail(id,limiterEnum);
        removeFromRedis(limiterEnum, dto.getLimitContent());
        limitService.removeById(id);
    }

    public void updateStatus(StatusRequest request, LimiterEnum limiterEnum) {
        LambdaUpdateWrapper<GatewayLimit> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(GatewayLimit::getId, request.getId());
        updateWrapper.eq(GatewayLimit::getStatus, request.getStatus());
        limitService.update(updateWrapper);
        LimitDto newDto = detail(request.getId(),limiterEnum);
        if (request.getStatus() == StatusEnum.ENABLE.value()) {
            syncToRedis(limiterEnum, newDto);
        }else{
            removeFromRedis(limiterEnum, newDto.getLimitContent());
        }
    }

    private void syncToRedis(LimiterEnum limiterEnum,LimitDto dto){
        LimiterConfig config = LimiterConfig.builder()
                .burstCapacity(dto.getLimitValue())
                .replenishRate(dto.getLimitValue())
                .limitHttpMethod(dto.getLimitHttpMethod())
                .requestedTokens(1).build();
        Map<String, Object> map = new HashMap<>(1);
        map.put(dto.getLimitContent(), config);
        reactiveRedisTemplate.opsForHash().putAll(limiterEnum.key(),map);


    }

    private void removeFromRedis(LimiterEnum limiterEnum,String member){
        reactiveRedisTemplate.opsForHash().remove(limiterEnum.key(), member);
    }
}
