package com.thlws.springcloud.gateway.internal.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 路由配置
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gateway_route")
public class GatewayRoute implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 路由编号,一般对应一个微服务
     */
    @TableField("route_id")
    private String routeId;

    /**
     * 服务路径,内部微服务 lb://开头
     */
    @TableField("route_uri")
    private String routeUri;

    /**
     * 1内部服务; 2外部地址
     */
    @TableField("route_type")
    private Integer routeType;

    /**
     * 匹配路径,eg:/api/user/**
     */
    @TableField("predicate_path")
    private String predicatePath;

    /**
     * 0禁用; 1启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 去除前缀类型 -1_N/A; 0_false 1_true
     */
    @TableField("strip_type")
    private Integer stripType;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
