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
 * 网关限流
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gateway_limit")
public class GatewayLimit implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 1主机限流; 2API限流; 3用户限流
     */
    @TableField("limit_type")
    private Integer limitType;

    /**
     * 限流阀值
     */
    @TableField("limit_value")
    private Integer limitValue;

    /**
     * 限流内容,可为 主机,API,用户
     */
    @TableField("limit_content")
    private String limitContent;

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
