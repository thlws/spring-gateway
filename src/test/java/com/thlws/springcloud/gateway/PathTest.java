package com.thlws.springcloud.gateway;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author HanleyTang 2020/8/1
 */
public class PathTest {

    public static void main(String[] args) {
        count();
    }

    public static void match(){
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

    public static void count(){
        int count = StrUtil.count("/api/user/**", "/") - 1;
        count = Math.max(count, 0);
        System.out.println(count);
    }

}
