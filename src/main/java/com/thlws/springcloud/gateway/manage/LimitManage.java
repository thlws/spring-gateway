package com.thlws.springcloud.gateway.manage;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.commons.data.PageRequest;
import com.thlws.commons.data.PageResult;
import com.thlws.springcloud.gateway.internal.enums.LimiterEnum;
import com.thlws.springcloud.gateway.internal.enums.StatusEnum;
import com.thlws.springcloud.gateway.internal.util.PageUtil;
import com.thlws.springcloud.gateway.limiter.config.LimiterConfig;
import com.thlws.springcloud.gateway.model.dto.LimitDto;
import com.thlws.springcloud.gateway.model.request.LimitRequest;
import com.thlws.springcloud.gateway.model.request.StatusRequest;
import com.thlws.springcloud.gateway.mybatis.model.GatewayLimit;
import com.thlws.springcloud.gateway.mybatis.service.GatewayLimitService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author HanleyTang 2020/8/8
 */
@Component
public class LimitManage {

    @Resource
    private GatewayLimitService limitService;

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    private  void page(PageRequest request){
        if (Objects.isNull(request.getPage())) {
            request.setPage(1);
        }
        if (Objects.isNull(request.getSize())) {
            request.setSize(20);
        }
    }

    public PageResult<LimitDto> list(LimitRequest request,LimiterEnum limiterEnum){

        PageUtil.build(request);

        LambdaQueryWrapper<GatewayLimit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GatewayLimit::getLimitType, limiterEnum.value());
        queryWrapper.eq(Objects.nonNull(request.getStatus()), GatewayLimit::getStatus, request.getStatus());
        queryWrapper.eq(Objects.nonNull(request.getLimitValue()), GatewayLimit::getLimitValue, request.getLimitValue());
        queryWrapper.like(Objects.nonNull(request.getLimitContent()), GatewayLimit::getLimitContent, request.getLimitContent());
        queryWrapper.like(Objects.nonNull(request.getLimitHttpMethod()), GatewayLimit::getLimitHttpMethod, request.getLimitHttpMethod());

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
        dto.setCreateTime(LocalDateTime.now());
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

        GatewayLimit limit = limitService.getById(request.getId());
        limit.setStatus(request.getStatus());
        limitService.updateById(limit);

        LimitDto newDto = Convert.convert(LimitDto.class, limit);
        if (request.getStatus() == StatusEnum.ENABLE.value()) {
            syncToRedis(limiterEnum, newDto);
        }else{
            removeFromRedis(limiterEnum, newDto.getLimitContent());
        }
    }

    private void syncToRedis(LimiterEnum limiterEnum,LimitDto dto){
        if (dto.getStatus() == StatusEnum.ENABLE.value()) {
            LimiterConfig config = LimiterConfig.builder()
                    .burstCapacity(dto.getLimitValue())
                    .replenishRate(dto.getLimitValue())
                    .limitHttpMethod(dto.getLimitHttpMethod())
                    .requestedTokens(1).build();
            Map<String, Object> map = new HashMap<>(1);
            map.put(dto.getLimitContent(), config);
            reactiveRedisTemplate.opsForHash().putAll(limiterEnum.key(),map);
        }
    }

    private void removeFromRedis(LimiterEnum limiterEnum,String member){
        reactiveRedisTemplate.opsForHash().remove(limiterEnum.key(), member);
    }
}
