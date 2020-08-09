package com.thlws.springcloud.gateway.mybatis.model;

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
 * @since 2020-08-09
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
    private String path;

    /**
     * 1放行; 2鉴权
     */
    @TableField("auth")
    private Integer auth;

    /**
     * 鉴权作用HTTP METHOD,全部为*
     */
    @TableField("auth_http_method")
    private String authHttpMethod;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
