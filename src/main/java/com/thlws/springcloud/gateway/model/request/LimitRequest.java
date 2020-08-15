package com.thlws.springcloud.gateway.model.request;

import com.thlws.commons.data.PageRequest;

import java.time.LocalDateTime;

/**
 * @author HanleyTang 2020/8/8
 */
public class LimitRequest extends PageRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 限流阀值
     */
    private Integer limitValue;

    /**
     * 限流内容,可为 主机,API,用户
     */
    private String limitContent;

    /**
     * 限流HTTP METHOD,多个逗号隔开，全部为 *
     */
    private String limitHttpMethod;

    /**
     * 0禁用; 1启用
     */
    private Integer status;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Integer limitValue) {
        this.limitValue = limitValue;
    }

    public String getLimitContent() {
        return limitContent;
    }

    public void setLimitContent(String limitContent) {
        this.limitContent = limitContent;
    }

    public String getLimitHttpMethod() {
        return limitHttpMethod;
    }

    public void setLimitHttpMethod(String limitHttpMethod) {
        this.limitHttpMethod = limitHttpMethod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
