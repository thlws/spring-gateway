package com.thlws.springcloud.gateway.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author HanleyTang
 */
@Data
public class LimitDto implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 1主机限流; 2API限流; 3用户限流
     */
//    private Integer limitType;

    /**
     * 限流阀值
     */
    @NotNull
    private Integer limitValue;

    /**
     * 限流内容,可为 主机,API,用户
     */
    @NotNull
    private String limitContent;

    /**
     * 限流HTTP METHOD,多个逗号隔开，全部为 *
     */
    @NotNull
    private String limitHttpMethod;

    /**
     * 0禁用; 1启用
     */
    @NotNull
    private Integer status;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;


}
