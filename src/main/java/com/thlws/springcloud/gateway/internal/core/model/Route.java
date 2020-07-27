package com.thlws.springcloud.gateway.internal.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 路由配置
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("route")
public class Route implements Serializable {

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
     * 顺序
     */
    @TableField("route_order")
    private Integer routeOrder;

    /**
     * 过滤 JSON
     */
    @TableField("filters")
    private String filters;

    /**
     * 断言 JSON
     */
    @TableField("predicates")
    private String predicates;

    /**
     * metadata JSON
     */
    @TableField("metadata")
    private String metadata;

    /**
     * 0禁用; 1启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
