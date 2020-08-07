package com.thlws.springcloud.gateway.internal.enums;

/**
 * @author HanleyTang 2020/8/7
 */
public enum StatusEnum {

    /**
     *
     */
    DISABLE(0,"禁用"),
    ENABLE(1,"可用");

    private int value;
    private String message;

    StatusEnum(int value, String message){
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
