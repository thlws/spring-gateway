package com.thlws.springcloud.gateway.internal.core.service.impl;

import com.thlws.springcloud.gateway.internal.core.model.ApiAuth;
import com.thlws.springcloud.gateway.internal.core.mapper.ApiAuthMapper;
import com.thlws.springcloud.gateway.internal.core.service.ApiAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网关限流 服务实现类
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-08-04
 */
@Service
public class ApiAuthServiceImpl extends ServiceImpl<ApiAuthMapper, ApiAuth> implements ApiAuthService {

}
