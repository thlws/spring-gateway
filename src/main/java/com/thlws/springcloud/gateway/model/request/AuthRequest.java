package com.thlws.springcloud.gateway.model.request;

import com.thlws.commons.data.PageRequest;

import java.time.LocalDateTime;

/**
 * @author HanleyTang 2020/8/8
 */
public class AuthRequest extends PageRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 支持REST动态路径表达式
     */
    private String path;

    /**
     * 1放行; 2鉴权
     */
    private Integer auth;

    /**
     * 鉴权作用HTTP METHOD,全部为*
     */
    private String authHttpMethod;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getAuthHttpMethod() {
        return authHttpMethod;
    }

    public void setAuthHttpMethod(String authHttpMethod) {
        this.authHttpMethod = authHttpMethod;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
