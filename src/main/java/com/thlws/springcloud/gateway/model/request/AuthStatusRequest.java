package com.thlws.springcloud.gateway.model.request;

import lombok.Data;

/**
 * @author HanleyTang 2020/8/8
 */
@Data
public class AuthStatusRequest {
    private long id;
    private int auth;
}
