package com.thlws.springcloud.gateway.model.request;

import lombok.Data;

/**
 * @author HanleyTang 2020/8/8
 */
@Data
public class StatusRequest {
    private long id;
    private int status;
}
