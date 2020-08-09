package com.thlws.springcloud.gateway.internal.enums;

/**
 * @author HanleyTang 2020/8/9
 */
public enum AuthEnum {

    /**
     *
     */
    PASS(1,"放行"),
    AUTH(2,"鉴权");

    private final int value;
    private final String message;

    AuthEnum(int value, String message){
        this.value = value;
        this.message  = message;
    }

    public int value(){
        return value;
    }

    public String message(){
        return message;
    }
}
