package com.thlws.springcloud.gateway.auth;

import com.alibaba.fastjson.JSON;
import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.auth.config.AuthConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author HanleyTang 2020/8/9
 */
@Slf4j
@Component
public class AuthProcessor {

    private final static String ALL_METHODS = "*";

    @Resource(name = "customerReactiveRedisTemplate")
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public Mono<Void> auth(ServerWebExchange exchange,
                            GatewayFilterChain chain,
                            String api,
                            AuthConfig config){

        //不满足HTTP METHOD 配置时,直接放行
        String httpMethod = exchange.getRequest().getMethodValue().toUpperCase();
        String authHttpMethod = config.getAuthHttpMethod().toUpperCase();
        if (!authHttpMethod.contentEquals(ALL_METHODS)
                && !authHttpMethod.contains(httpMethod)) {
            return Mono.empty();
        }


        //FIXME 鉴权机制
        List<String> tokens =  exchange.getRequest().getHeaders().get("token");
        if (!CollectionUtils.isEmpty(tokens)){
            return Mono.empty();
        }

        log.warn("request has been rejected api=[{}]",api);

        ApiResult<String> apiResult = ApiResult.error(HttpStatus.FORBIDDEN.value(), "缺少认证信息，无权访问。");
        byte [] bytes = JSON.toJSONString(apiResult).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(Mono.just(buffer));

    }

}
