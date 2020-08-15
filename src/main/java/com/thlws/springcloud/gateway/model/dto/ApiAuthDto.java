package com.thlws.springcloud.gateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author HanleyTang 2020/8/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiAuthDto {

    /**
     * ID
     */
    private Long id;

    /**
     * 支持REST动态路径表达式
     */
    @NotNull
    private String path;

    /**
     * 1放行; 2鉴权
     */
    @NotNull
    private Integer auth;

    /**
     * 鉴权作用HTTP METHOD,全部为*
     */
    @NotNull
    private String authHttpMethod;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;
}
