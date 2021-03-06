package com.thlws.springcloud.gateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 路由配置
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-08-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRouteDto implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 路由编号,一般对应一个微服务
     */
    @NotNull
    private String routeId;

    /**
     * 服务路径,内部微服务 lb://开头
     */
    @NotNull
    private String routeUri;

    /**
     * 1内部服务; 2外部地址
     */
    @NotNull
    private Integer routeType;

    /**
     * 匹配路径,eg:/api/user/**
     */
    @NotNull
    private String predicatePath;

    /**
     * 0禁用; 1启用
     */
    @NotNull
    private Integer status;

    /**
     * 去除前缀类型 -1_N/A; 0_false 1_true
     */
    @NotNull
    private Integer stripType;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;


}
