package com.thlws.springcloud.gateway.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author HanleyTang 2020/8/8
 */
@Data
public class AuthStatusRequest {

    @NotNull
    private long id;

    @NotNull
    private int auth;
}
