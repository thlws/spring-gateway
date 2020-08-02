//package com.thlws.springcloud.gateway.config;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * 接口限流过滤器
// */
//@Component
//public class PathRateGlobalFilter implements Ordered, GlobalFilter {
//
//    @Autowired
//    private MyRedisRateLimiter myRedisRateLimiter;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().value();
//        //TODO 从数据库中获取到该接口对于的限流参数
//
//        //如果允许同行，没有超过该接口的流量限制
//        if (myRedisRateLimiter.isAllowed("path:" + path + ":",
//                10,
//                10)) {
//            return chain.filter(exchange);
//        }
//        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
//        return exchange.getResponse().setComplete();
//    }
//
//    @Override
//    public int getOrder() {
//        return 1;
//    }
//}