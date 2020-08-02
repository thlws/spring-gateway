package com.thlws.springcloud.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

/**
 * @author HanleyTang 2020/8/1
 */
public class PathTest {

    public static void main(String[] args) {
        PathMatcher matcher = new AntPathMatcher();
        String pattern="/api/user/*";//路径匹配模式

        String requestPath="/api/user/";//请求路径

        boolean result = matcher.match(pattern, requestPath);
        System.out.println(result);

        boolean result2 = matcher.match(
                "/api/{users}/{id}",
                "/api/3/33/12");

        System.out.println(result2);

    }


}
