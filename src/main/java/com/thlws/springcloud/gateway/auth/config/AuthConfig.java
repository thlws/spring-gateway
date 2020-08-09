package com.thlws.springcloud.gateway.auth.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HanleyTang 2020/8/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig {

    /**
     * 支持REST动态路径表达式
     */
    private String path;

    private String authHttpMethod;
}
