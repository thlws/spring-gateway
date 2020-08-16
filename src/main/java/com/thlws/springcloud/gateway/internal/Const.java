package com.thlws.springcloud.gateway.internal;

/**
 * @author HanleyTang 2020/8/9
 */
public interface Const {

    interface Key{
        String AUTH_API = "gateway:auth:api";
    }

    interface Sort{
        String ID_DESC = "-id";
        String ID_ASC = "+id";
    }
}
