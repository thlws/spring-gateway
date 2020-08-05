package com.thlws.springcloud.gateway.internal.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author HanleyTang 2020/8/4
 */
public class PathUtil {

    private final static PathMatcher matcher = new AntPathMatcher();

    public static boolean match(String pattern, String path){
        return matcher.match(pattern, path);
    }

}
