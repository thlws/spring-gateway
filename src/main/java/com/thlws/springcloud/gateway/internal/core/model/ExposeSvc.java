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
 * 暴露服务
 * </p>
 *
 * @author Mybatis plus generator
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("expose_svc")
public class ExposeSvc implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 服务说明
     */
    @TableField("remark")
    private String remark;

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
