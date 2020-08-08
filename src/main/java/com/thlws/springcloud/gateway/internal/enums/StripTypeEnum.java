package com.thlws.springcloud.gateway.internal.enums;

/**
 * @author HanleyTang 2020/8/7
 */
public enum StripTypeEnum {

    /**
     * NA
     */
    NA(-1,"N/A"),
    FALSE(0,"否"),
    TRUE(1,"是");

    private final int value;
    private final String message;

    StripTypeEnum(int value, String message){
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
