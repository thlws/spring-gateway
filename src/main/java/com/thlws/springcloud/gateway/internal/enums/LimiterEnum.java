package com.thlws.springcloud.gateway.internal.enums;

/**
 * @author HanleyTang 2020/8/8
 */
public enum LimiterEnum {

    /**
     * 1主机限流; 2API限流; 3用户限流
     */
    HOST(1,"limiter:config:host","1主机限流"),
    API(2,"limiter:config:api","API限流"),
    USER(3,"limiter:config:user","用户限流");

    private final int value;
    private final String key;
    private final String message;

    LimiterEnum(int value, String key, String message){
        this.value = value;
        this.key = key;
        this.message  = message;
    }

    public String key(){
        return key;
    }

    public int value(){
        return value;
    }

    public String message(){
        return message;
    }
}
