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
 * 暴露API
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("export_api")
public class ExportApi implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 微服务名称
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 服务路径
     */
    @TableField("service_path")
    private String servicePath;

    /**
     * 0禁用; 1启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 0放行; 1鉴权
     */
    @TableField("auth")
    private Integer auth;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
