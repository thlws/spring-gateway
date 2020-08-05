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
 * 网关限流
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("api_auth")
public class ApiAuth implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 支持REST动态路径表达式
     */
    @TableField("path")
    private Integer path;

    /**
     * 0放行; 1鉴权
     */
    @TableField("auth")
    private Integer auth;

    /**
     * 鉴权作用HTTP METHOD
     */
    @TableField("http_method")
    private String httpMethod;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
